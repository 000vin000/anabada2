/**
 * 2차비번 여부 확인 해서 경로 이동해주는거
 */

export function goToUpdateInfo() {
  const token = localStorage.getItem("Token");

  fetch("/user/pin/status", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((res) => res.json())
    .then((data) => {
      if (data.hasPin) {
        window.location.href = "/user/pin/pinAuth.html";
      } else {
        window.location.href = "/user/pin/createPin.html";
      }
    })
    .catch((error) => {
      console.error("2차 비밀번호 상태 확인 실패:", error);
      alert("2차 인증 확인 중 오류가 발생했습니다.");
    });
}

// 이벤트 바인딩
document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("updateInfoBtn");
  if (btn) {
    btn.addEventListener("click", goToUpdateInfo);
  }
});


/*
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
		  window.location.href = "/user/pin/pinAuth.html";     
		} else {
		  window.location.href = "/user/pin/createPin.html";
    }
  } catch (e) {
    alert("서버 요청 중 오류가 발생했습니다.");
    console.error("2차 비밀번호 상태 확인 실패", e);
  }
}

*/