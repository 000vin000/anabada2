document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("loginBtn").addEventListener("click", loginUser);
});

function loginUser() {
    let userId = document.getElementById("userId").value;
    let userPw = document.getElementById("userPw").value;

    const loginData = {
        userId: userId,
        userPw: userPw
    };

    fetch("/userlogin/login", {  // ✅ API 엔드포인트 유지
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.message === "로그인 성공!") {
            alert(data.message);
            window.location.href = "/dashboard.html";  // 로그인 성공 후 페이지 이동
        } else {
            alert(data.message);
        }
    })
    .catch(error => {
        console.error("로그인 오류:", error);
        alert("로그인 실패: " + error.message);
    });
}
