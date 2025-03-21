/**
 * 토큰이 있으면 자동 로그인 유지
 */
document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("authToken");

    if (token) {
        console.log("로그인 상태 유지됨");
        fetch("/userlogin/check", {
            method: "GET",
            headers: { "Authorization": "Bearer " + token }
        })
        .then(response => response.json())
        .then(data => {
            if (!data.authenticated) {
                localStorage.removeItem("authToken"); // 만료되면 토큰 삭제
            }
        })
        .catch(error => {
            console.error("로그인 확인 오류:", error);
            localStorage.removeItem("authToken");
        });
    }
});
