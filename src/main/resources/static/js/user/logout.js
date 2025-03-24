document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("logoutBtn").addEventListener("click", function () {
        localStorage.removeItem("authToken"); 
        window.location.href = "/auth/login.html"; //로그인 페이지로
    });
});
