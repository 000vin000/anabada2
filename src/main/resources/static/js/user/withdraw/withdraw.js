document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("withdrawForm");
    const reasonInput = document.getElementById("reason");
    const resultMessage = document.getElementById("resultMessage");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const reason = reasonInput.value.trim();
        if (!reason) {
            resultMessage.textContent = "탈퇴 사유를 입력해주세요.";
            return;
        }

        const token = localStorage.getItem("Token"); 
        if (!token) {
            resultMessage.textContent = "로그인 정보가 없습니다.";
            return;
        }

        try {
            const response = await fetch("/user/withdraw", {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ reason })
            });

            if (response.ok) {
                resultMessage.textContent = "정상적으로 탈퇴 처리되었습니다.";
                localStorage.removeItem("Token"); 
                setTimeout(() => {
                    window.location.href = "/"; 
                }, 1500);
            } else {
                const data = await response.json();
                resultMessage.textContent = data.error || "탈퇴 처리에 실패했습니다.";
            }
        } catch (err) {
            console.error("탈퇴 요청 실패:", err);
            resultMessage.textContent = "서버 오류가 발생했습니다.";
        }
    });
});
