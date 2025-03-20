document.addEventListener("DOMContentLoaded", function () {
    console.log("register.js 로드됨");
    document.getElementById("registerBtn").addEventListener("click", registerUser);
});

function registerUser() {
    // 요소 가져오기
    let userId = document.getElementById("userId").value;
    let userPw = document.getElementById("userPw").value;
    let userName = document.getElementById("userName").value;
    let userNick = document.getElementById("userNick").value;
    let userEmail = document.getElementById("userEmail").value;
    // 전화번호 3칸 합치기
    let phone1 = document.getElementById("phone1").value;
    let phone2 = document.getElementById("phone2").value;
    let phone3 = document.getElementById("phone3").value;
    let userPhone = phone1 + "-" + phone2 + "-" + phone3;
    // 주소: 기본주소와 상세주소
    let baseAddress = document.getElementById("baseAddress").value;
    let detailAddress = document.getElementById("detailAddress").value;

    const userData = {
        userId: userId,
        userPw: userPw,
        userName: userName,
        userNick: userNick,
        userEmail: userEmail,
        userPhone: userPhone,
        baseAddress: baseAddress,
        detailAddress: detailAddress,
        userType: "INDIVIDUAL"  // 프론트에서 문자열로 보내면, UserDTO가 ENUM으로 자동 변환됨
    };

    console.log("회원가입 요청 데이터:", JSON.stringify(userData));

    fetch("/user/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message);
        window.location.href = "/login.html";
    })
    .catch(error => {
        console.error("회원가입 오류:", error);
        alert("회원가입 오류: " + error.message);
    });
}
