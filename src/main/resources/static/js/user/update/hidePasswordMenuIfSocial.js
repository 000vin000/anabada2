document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("Token");
    if (!token) return;

    try {
        const payload = JSON.parse(atob(token.split('.')[1]));

        if (payload.roles && payload.roles.includes("ROLE_SOCIAL")) {
            const pwBtn = document.getElementById("updatePwBtn");
            if (pwBtn) {
                pwBtn.style.display = "none";
            }
        }
    } catch (e) {
        console.error("토큰 디코딩 실패:", e);
    }
});
