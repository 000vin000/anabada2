/**
 * 서버와 통신 (로그인 유지 체크, 재발급 등)
 */
import { getAccessToken, setAccessToken, removeAccessToken } from "./tokenUtil.js";

export async function checkAuth() {
  const token = getAccessToken();
  console.log("저장된 토큰:", token);
  if (!token) return false;

  try {
    const res = await 	fetch("/login/individual/check", {
      method: "GET",
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      }
    });

    console.log("/userlogin/check 응답 상태:", res.status);

    if (res.status === 401) {
      console.warn("Access Token 만료 Refresh Token으로 재발급 시도");
      console.log("재발급 요청 실행");

      const reissue = await fetch("/auth/reissue", { method: "POST" });

      console.log("재발급 응답 상태:", reissue.status);

      if (reissue.ok) {
        const { accessToken } = await reissue.json();
        setAccessToken(accessToken);
        console.log("새 accessToken 저장됨:", accessToken);
        return true;
      } else {
        console.warn("Refresh Token 만료 또는 없음 로그아웃 처리");
        removeAccessToken();
        return false;
      }
    }

    const data = await res.json();
    console.log("인증 체크 결과:", data);
    return data.authenticated;

  } catch (err) {
    console.error("Auth check error:", err);
    removeAccessToken();
    return false;
  }
}
