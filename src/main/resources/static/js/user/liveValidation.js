/**
 * 실시간 유효성검사
 */

document.addEventListener("DOMContentLoaded", function () {
    console.log("liveValidation.js 로드됨");

    // 실시간
    document.getElementById("userId").addEventListener("input", validateUserId);
    document.getElementById("userPw").addEventListener("input", validatePassword);
    document.getElementById("userPw2").addEventListener("input", validatePasswordMatch);
    document.getElementById("phone3").addEventListener("input", validatePhoneNumber);
    document.getElementById("userEmail").addEventListener("input", validateEmail);

    // 중복 체크 버튼
    document.getElementById("checkUserIdBtn").addEventListener("click", checkUserId);
    document.getElementById("checkUserNickBtn").addEventListener("click", checkUserNick);
});

// 아이디 중복 체크
function checkUserId() {
    const userId = document.getElementById("userId").value;
    const userIdCheckMessage = document.getElementById("userIdCheckMessage");

    if (userId.length < 4) {
        userIdCheckMessage.textContent = "아이디는 최소 4자 이상이어야 합니다.";
        userIdCheckMessage.style.color = "red";
        return;
    }

    fetch(`/userjoin/checkUserId?userId=${userId}`)
        .then(response => response.json())
        .then(data => {
            if (data.available) {
                userIdCheckMessage.textContent = "사용 가능한 아이디입니다.";
                userIdCheckMessage.style.color = "green";
            } else {
                userIdCheckMessage.textContent = "이미 사용 중인 아이디입니다.";
                userIdCheckMessage.style.color = "red";
            }
        })
        .catch(error => {
            console.error("아이디 중복 체크 오류:", error);
            userIdCheckMessage.textContent = "오류 발생. 다시 시도해주세요.";
            userIdCheckMessage.style.color = "red";
        });
}

// 닉네임 중복 체크
function checkUserNick() {
    const userNick = document.getElementById("userNick").value;
    const userNickCheckMessage = document.getElementById("userNickCheckMessage");

    if (userNick.length < 2) {
        userNickCheckMessage.textContent = "닉네임은 최소 2자 이상이어야 합니다.";
        userNickCheckMessage.style.color = "red";
        return;
    }

    fetch(`/userjoin/checkUserNick?userNick=${userNick}`)
        .then(response => response.json())
        .then(data => {
            if (data.available) {
                userNickCheckMessage.textContent = "사용 가능한 닉네임입니다.";
                userNickCheckMessage.style.color = "green";
            } else {
                userNickCheckMessage.textContent = "이미 사용 중인 닉네임입니다.";
                userNickCheckMessage.style.color = "red";
            }
        })
        .catch(error => {
            console.error("닉네임 중복 체크 오류:", error);
            userNickCheckMessage.textContent = "오류 발생. 다시 시도해주세요.";
            userNickCheckMessage.style.color = "red";
        });
}


// 아이디 유효성 검사
function validateUserId() {
    const userIdInput = document.getElementById("userId");
    const userIdWarning = document.getElementById("userIdWarning");
    const userIdPattern = /^[a-zA-Z0-9]{4,20}$/;

    if (!userIdPattern.test(userIdInput.value)) {
        userIdWarning.textContent = "영어와 숫자만 입력 가능합니다. (4~20자)";
        userIdWarning.style.color = "red";
    } else {
        userIdWarning.textContent = ""; 
    }
}

// 비밀번호 유효성 검사
function validatePassword() {
    const passwordInput = document.getElementById("userPw");
    const passwordWarning = document.getElementById("passwordWarning");
    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/;

    if (!passwordPattern.test(passwordInput.value)) {
        passwordWarning.textContent = "비밀번호는 8~20자, 영문+숫자+특수문자를 포함해야 합니다.";
        passwordWarning.style.color = "red";
    } else {
        passwordWarning.textContent = ""; 
    }
    validatePasswordMatch(); 
}

// 비밀번호 확인 검사
function validatePasswordMatch() {
    const passwordInput = document.getElementById("userPw");
    const passwordConfirmInput = document.getElementById("userPw2");
    const passwordMatchWarning = document.getElementById("passwordMatchWarning");

    if (passwordInput.value !== passwordConfirmInput.value) {
        passwordMatchWarning.textContent = "비밀번호가 일치하지 않습니다.";
        passwordMatchWarning.style.color = "red";
    } else {
        passwordMatchWarning.textContent = ""; 
    }
}

// 이메일 유효성 검사 
function validateEmail() {
    const emailInput = document.getElementById("userEmail");
    const emailWarning = document.getElementById("emailWarning");
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!emailPattern.test(emailInput.value)) {
        emailWarning.textContent = "올바른 이메일 형식이 아닙니다.";
        emailWarning.style.color = "red";
    } else {
        emailWarning.textContent = "";
    }
}

// 전화번호 유효성 검사
function validatePhoneNumber() {
    let phone1 = document.getElementById("phone1").value;
    let phone2 = document.getElementById("phone2").value;
    let phone3 = document.getElementById("phone3").value;
    let userPhone = phone1 + "-" + phone2 + "-" + phone3;

    const phoneWarning = document.getElementById("phoneWarning");
    const phonePattern = /^\d{3}-\d{3,4}-\d{4}$/;

    if (!phonePattern.test(userPhone)) {
        phoneWarning.textContent = "올바른 전화번호 형식이 아닙니다.";
        phoneWarning.style.color = "red";
    } else {
        phoneWarning.textContent = "";
    }
}
