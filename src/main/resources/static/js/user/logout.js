
document.addEventListener("DOMContentLoaded", function () {
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function () {
            console.log("로그아웃 시도");
            localStorage.removeItem("Token");
			localStorage.removeItem("refreshToken");
            window.location.href = "/";
        });
    }
});
