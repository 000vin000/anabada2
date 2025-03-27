/**
 * 2차비번 여부 확인 해서 경로 이동해주는거
 */

export async function goToUpdateInfo() {
  try {
    const res = await fetch("/user/pin/status", {
      method: "GET",
      headers: {
        "Authorization": "Bearer " + localStorage.getItem("Token")
      }
    });

    if (!res.ok) {
      alert("2차 비밀번호 상태 확인에 실패했습니다.");
      return;
    }

    const data = await res.json();

    if (data.hasPin) {
      location.href = "/user/pinauth.html"; // ✅ updatepin → pinauth 로 변경됨
    } else {
      location.href = "/user/createpin.html";
    }
  } catch (e) {
    alert("서버 요청 중 오류가 발생했습니다.");
    console.error("2차 비밀번호 상태 확인 실패", e);
  }
}
