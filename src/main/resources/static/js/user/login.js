document.addEventListener("DOMContentLoaded", function () {
    console.log("login.js 로드됨");
    document.getElementById("loginBtn").addEventListener("click", loginUser);
});

function loginUser() {
    // 기존 토큰 제거
    localStorage.removeItem("Token");
    localStorage.removeItem("refreshToken");

    const userId = document.getElementById("userId").value;
    const userPw = document.getElementById("userPw").value;

    const loginData = {
        userId: userId,
        userPw: userPw
    };

    console.log("로그인 요청 데이터:", JSON.stringify(loginData));

    fetch("/userlogin/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginData)
    })
    .then(response => response.json())
    .then(data => {
        console.log("로그인 응답 데이터:", data);

        if (data.message) alert(data.message);

        if (data.accessToken) {
            localStorage.setItem("Token", data.accessToken);
            localStorage.setItem("refreshToken", data.refreshToken);
            console.log("토큰 저장 완료");

            //관리자 페이지에 미리 요청해서 권한 확인 후 이동
            fetch("/admin/dashboard", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + data.accessToken
                }
            })
            .then(res => {
                if (res.ok) {
                    console.log("관리자 권한 확인 완료, 이동 중...");
                    window.location.href = "/admin/dashboard";
                } else {
                    console.warn("권한 없음 또는 일반 유저, 홈으로 이동");
                    window.location.href = "/";
                }
            });
        } else {
            console.warn("accessToken 없음. 로그인 실패");
        }
    })
    .catch(error => {
        console.error("로그인 오류:", error);
        alert("로그인 오류: " + error.message);
    });
}
