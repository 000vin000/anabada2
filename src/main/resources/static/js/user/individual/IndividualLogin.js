document.addEventListener("DOMContentLoaded", function () {
    console.log("login.js 로드됨");
    document.getElementById("loginBtn").addEventListener("click", loginUser);
});

function loginUser() {
    let userId = document.getElementById("userId").value;
    let userPw = document.getElementById("userPw").value;

    const loginData = {
        userId: userId,
        userPw: userPw
    };

    console.log("로그인 요청 데이터:", JSON.stringify(loginData));

	fetch("/login/individual/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginData)
    })
    .then(response => response.json())
    .then(data => {
        console.log("로그인 응답 데이터:", data);

        if (data.message) alert(data.message);

        // accessToken이 있으면 저장
        if (data.accessToken) {
            console.log("토큰 저장됨:", data.accessToken);
            localStorage.setItem("Token", data.accessToken);
        } else {
            console.warn("accessToken 없음. 로그인 실패");
        }

        // 메인 또는 리디렉션
        if (data.redirectUrl) {
            window.location.href = data.redirectUrl;
        } else {
            window.location.href = "/";
        }
    })
    .catch(error => {
        console.error("로그인 오류:", error);
        alert("로그인 오류: " + error.message);
    });
}
