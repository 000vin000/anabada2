/**
 요청할 때 Access Token을 자동으로 헤더에 붙이고,
 401이면 /auth/reissue로 재발급 시도,
 재시도까지 자동
 */

import { getAccessToken, setAccessToken, removeAccessToken } from "./tokenUtil.js";

export async function fetchWithAuth(url, options = {}) {
  const token = getAccessToken();

  const headers = options.headers || {};
  headers["Authorization"] = "Bearer " + token;
  headers["Content-Type"] = "application/json";
  options.headers = headers;

  let response = await fetch(url, options);

  if (response.status === 401) {
    console.warn("Access Token 만료됨 재발급 시도");

    const reissueRes = await fetch("/auth/reissue", { method: "POST" });

    if (reissueRes.ok) {
      const { accessToken } = await reissueRes.json();
      setAccessToken(accessToken);
      headers["Authorization"] = "Bearer " + accessToken;

      // 재시도
      response = await fetch(url, options);
    } else {
      console.warn("Refresh Token도 만료 → 로그아웃 처리");
      removeAccessToken();
      window.location.href = "/login";
    }
  }

  return response;
}
