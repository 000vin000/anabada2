document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("updatePinForm");
  const resultMessage = document.getElementById("resultMessage");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const newPin = document.getElementById("newPin").value.trim();
    const confirmPin = document.getElementById("confirmPin").value.trim();

    if (newPin.length < 4 || newPin.length > 6) {
      resultMessage.textContent = "2차 비밀번호는 4~6자리 숫자여야 합니다.";
      return;
    }

    if (newPin !== confirmPin) {
      resultMessage.textContent = "2차 비밀번호가 일치하지 않습니다.";
      return;
    }

    const token = localStorage.getItem("Token");
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    try {
      const res = await fetch("/user/update/pin", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          newPin 
        }),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text);
      }

      alert("2차 비밀번호가 성공적으로 변경되었습니다.");
      location.href = "/mypage";

    } catch (err) {
      console.error("변경 실패:", err);
      resultMessage.textContent = err.message || "서버 오류가 발생했습니다.";
    }
  });
});
