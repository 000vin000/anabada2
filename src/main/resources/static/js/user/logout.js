document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("logoutBtn").addEventListener("click", function () {
        localStorage.removeItem("Token");
        window.location.href = "/auth/login.html";
    });
});
