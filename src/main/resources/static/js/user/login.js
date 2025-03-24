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
		    console.log("토큰 저장됨:", data.accessToken);
		    localStorage.setItem("Token", data.accessToken); //키 값으로 저장
		} else {
		    console.warn("accessToken 없음. 로그인 실패");
		}

        // 메인으로
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
