import {
  validatePasswordForUpdate,
  isPasswordMatch
} from "/js/user/common/liveValidation.js";

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("updatePasswordForm");
  const resultMessage = document.getElementById("resultMessage");

  if (!form || !resultMessage) return;

  // ✅ 실시간 유효성 검사 이벤트
  document.getElementById("newPassword").addEventListener("input", () => {
    const pw = document.getElementById("newPassword").value;
    const warning = document.getElementById("passwordWarning");

    if (!validatePasswordForUpdate(pw)) {
      warning.textContent = "비밀번호는 8~20자, 영문+숫자+특수문자를 포함해야 합니다.";
    } else {
      warning.textContent = "";
    }
  });

  document.getElementById("newPassword2").addEventListener("input", () => {
    const pw1 = document.getElementById("newPassword").value;
    const pw2 = document.getElementById("newPassword2").value;
    const warning = document.getElementById("passwordMatchWarning");

    if (!isPasswordMatch(pw1, pw2)) {
      warning.textContent = "비밀번호가 일치하지 않습니다.";
    } else {
      warning.textContent = "";
    }
  });

  // ✅ 제출 이벤트
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const currentPassword = document.getElementById("currentPassword")?.value;
    const newPassword = document.getElementById("newPassword")?.value;
    const newPassword2 = document.getElementById("newPassword2")?.value;

    if (!validatePasswordForUpdate(newPassword)) {
      resultMessage.textContent = "비밀번호 조건을 다시 확인해주세요.";
      return;
    }

    if (!isPasswordMatch(newPassword, newPassword2)) {
      resultMessage.textContent = "비밀번호가 서로 일치하지 않습니다.";
      return;
    }

    const token = localStorage.getItem("Token");
    if (!token) {
      resultMessage.textContent = "로그인 정보가 없습니다.";
      return;
    }

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (payload.roles && payload.roles.includes("ROLE_SOCIAL")) {
        resultMessage.textContent = "소셜 로그인 계정은 비밀번호를 변경할 수 없습니다.";
        return;
      }
    } catch (err) {
      console.error("토큰 디코딩 오류:", err);
      resultMessage.textContent = "로그인 상태를 확인할 수 없습니다.";
      return;
    }

    try {
      const res = await fetch("/user/update/password", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ currentPassword, newPassword })
      });

      if (res.ok) {
        resultMessage.style.color = "green";
        resultMessage.textContent = "비밀번호가 성공적으로 변경되었습니다.";
        setTimeout(() => location.href = "/", 1500);
      } else {
        const text = await res.text();
        resultMessage.textContent = "변경 실패: " + text;
      }
    } catch (err) {
      console.error("비밀번호 변경 오류:", err);
      resultMessage.textContent = "서버 오류가 발생했습니다.";
    }
  });
});
