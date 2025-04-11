/**
 * 2차비번 여부 확인해서 경로 이동 (회원정보 수정/탈퇴 등 공통)
 */
export function goToUpdateInfo(targetUrl) {
  const token = localStorage.getItem("Token");

  fetch("/user/pin/status", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((res) => res.json())
    .then((data) => {
      const encodedNext = encodeURIComponent(targetUrl);
      if (data.hasPin) {
        window.location.href = `/user/pin/pinAuth.html?next=${encodedNext}`;
      } else {
        window.location.href = `/user/pin/createPin.html?next=${encodedNext}`;
      }
    })
    .catch((error) => {
      console.error("2차 비밀번호 상태 확인 실패:", error);
      alert("2차 인증 확인 중 오류가 발생했습니다.");
    });
}

// DOM 바인딩 예시 (회원정보 수정 버튼)
document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("updateInfoBtn");
  if (btn) {
    btn.addEventListener("click", () => {
      goToUpdateInfo("/user/update/individual/IndividualUpdateinfo.html");
    });
  }

  const withdrawBtn = document.getElementById("withdrawBtn");
  if (withdrawBtn) {
    withdrawBtn.addEventListener("click", () => {
      goToUpdateInfo("/user/withdraw/withdraw.html");
    });
  }
  const pwUpdateBtn = document.getElementById("updatePwBtn");
  if (pwUpdateBtn) {
    pwUpdateBtn.addEventListener("click", () => {
      goToUpdateInfo("/user/update/updatepw.html"); 
    });
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