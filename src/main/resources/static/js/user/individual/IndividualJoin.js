document.addEventListener("DOMContentLoaded", function () {
    console.log("join.js 로드됨");
    document.getElementById("joinBtn").addEventListener("click", joinUser);
});

function joinUser() {
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

    // 최종 제출 시 유효성 검사
    if (userPw !== userPwConfirm) {
        alert("비밀번호가 일치하지 않습니다.");
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

	fetch("/join/individual/join", {
	    method: "POST",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify(userData)
	})
	.then(response => response.json())
	.then(data => {
	    if (data.error) {
	        alert(data.error); // 에러 메시지 출력 (중복된 아이디, 닉네임 등)
	    } else {
	        alert(data.message);  // 회원가입 성공 메시지
	        window.location.href = data.redirectUrl;  // 로그인 페이지로 이동
	    }
	})
	.catch(error => {
	    console.error("회원가입 오류:", error);
	    alert("회원가입 오류: " + error.message);
	});
}
