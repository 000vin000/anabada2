document.addEventListener("DOMContentLoaded", function () {
    console.log("register.js 로드됨");
    document.getElementById("registerBtn").addEventListener("click", registerUser);
});

function registerUser() {
    let userId = document.getElementById("userId").value;
    let userPw = document.getElementById("userPw").value;
    let userPwConfirm = document.getElementById("userPw2").value;
    let userName = document.getElementById("userName").value;
    let userNick = document.getElementById("userNick").value;
    let userEmail = document.getElementById("userEmail").value;

    // 전화번호 조합
    let phone1 = document.getElementById("phone1").value;
    let phone2 = document.getElementById("phone2").value;
    let phone3 = document.getElementById("phone3").value;
    let userPhone = phone1 + "-" + phone2 + "-" + phone3;

    let baseAddress = document.getElementById("baseAddress").value;
    let detailAddress = document.getElementById("detailAddress").value;

    // 비밀번호 확인 검증 추가
    if (userPw !== userPwConfirm) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
    }

    // 전화번호 검증 (숫자만 입력 가능)
    const phonePattern = /^\d{3}-\d{3,4}-\d{4}$/;
    if (!phonePattern.test(userPhone)) {
        alert("올바른 전화번호 형식이 아닙니다.");
        return;
    }

    const userData = {
        userId: userId,
        userPw: userPw,
        userName: userName,
        userNick: userNick,
        userEmail: userEmail,
        userPhone: userPhone,
        baseAddress: baseAddress,
        detailAddress: detailAddress,
        userType: "INDIVIDUAL"
    };

    console.log("회원가입 요청 데이터:", JSON.stringify(userData));

	fetch("/userjoin/register", {  // 기존 API 경로 유지
	    method: "POST",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify(userData)
	})
    .then(response => response.json())
    .then(data => {
        alert(data.message);
        window.location.href = "/login.html";  // 회원가입 후 로그인 페이지로 이동
    })
    .catch(error => {
        console.error("회원가입 오류:", error);
        alert("회원가입 오류: " + error.message);
    });
}
