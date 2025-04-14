/**
 * 실시간 유효성검사 (모든 회원기능 공통용)
 */
document.addEventListener("DOMContentLoaded", function () {
    console.log("liveValidation.js 로드됨");

    const userIdInput = document.getElementById("userId");
    const userPwInput = document.getElementById("userPw");
    const userPw2Input = document.getElementById("userPw2");
    const phone3Input = document.getElementById("phone3");
    const userEmailInput = document.getElementById("userEmail");
    const checkUserIdBtn = document.getElementById("checkUserIdBtn");
    const checkUserNickBtn = document.getElementById("checkUserNickBtn");

    if (userIdInput) userIdInput.addEventListener("input", validateUserId);
    if (userPwInput) userPwInput.addEventListener("input", validatePassword);
    if (userPw2Input) userPw2Input.addEventListener("input", validatePasswordMatch);
    if (phone3Input) phone3Input.addEventListener("input", validatePhoneNumber);
    if (userEmailInput) userEmailInput.addEventListener("input", validateEmail);

    if (checkUserIdBtn) checkUserIdBtn.addEventListener("click", checkUserId);
    if (checkUserNickBtn) checkUserNickBtn.addEventListener("click", checkUserNick);
});

function checkUserId() {
    const userId = document.getElementById("userId")?.value;
    const message = document.getElementById("userIdCheckMessage");

    if (!userId || !message) return;

    fetch(`/join/individual/checkUserId?userId=${encodeURIComponent(userId)}`)
        .then(res => {
            if (!res.ok) throw new Error("서버 오류");
            return res.json();
        })
        .then(data => {
            if (data.available) {
                message.textContent = "사용 가능한 아이디입니다.";
                message.style.color = "green";
            } else {
                message.textContent = "이미 사용 중인 아이디입니다.";
                message.style.color = "red";
            }
        })
        .catch(err => {
            console.error("아이디 중복 확인 오류:", err);
            message.textContent = "오류가 발생했습니다.";
            message.style.color = "red";
        });
}

function checkUserNick() {
    const userNick = document.getElementById("userNick")?.value;
    const message = document.getElementById("userNickCheckMessage");

    if (!userNick || !message) return;

    fetch(`/join/individual/checkUserNick?userNick=${encodeURIComponent(userNick)}`)
        .then(res => {
            if (!res.ok) throw new Error("서버 오류");
            return res.json();
        })
        .then(data => {
            if (data.available) {
                message.textContent = "사용 가능한 닉네임입니다.";
                message.style.color = "green";
            } else {
                message.textContent = "이미 사용 중인 닉네임입니다.";
                message.style.color = "red";
            }
        })
        .catch(err => {
            console.error("닉네임 중복 확인 오류:", err);
            message.textContent = "오류가 발생했습니다.";
            message.style.color = "red";
        });
}


function validateUserId() {
    const userIdInput = document.getElementById("userId");
    const userIdWarning = document.getElementById("userIdWarning");
    const userIdPattern = /^[a-zA-Z0-9]{4,20}$/;

    if (!userIdInput || !userIdWarning) return;

    if (!userIdPattern.test(userIdInput.value)) {
        userIdWarning.textContent = "영어와 숫자만 입력 가능합니다. (4~20자)";
        userIdWarning.style.color = "red";
    } else {
        userIdWarning.textContent = "";
    }
}

function validatePassword() {
    const passwordInput = document.getElementById("userPw");
    const passwordWarning = document.getElementById("passwordWarning");
    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/;

    if (!passwordInput || !passwordWarning) return;

    if (!passwordPattern.test(passwordInput.value)) {
        passwordWarning.textContent = "비밀번호는 8~20자, 영문+숫자+특수문자를 포함해야 합니다.";
        passwordWarning.style.color = "red";
    } else {
        passwordWarning.textContent = "";
    }

    validatePasswordMatch();
}

function validatePasswordMatch() {
    const passwordInput = document.getElementById("userPw");
    const passwordConfirmInput = document.getElementById("userPw2");
    const passwordMatchWarning = document.getElementById("passwordMatchWarning");

    if (!passwordInput || !passwordConfirmInput || !passwordMatchWarning) return;

    if (passwordInput.value !== passwordConfirmInput.value) {
        passwordMatchWarning.textContent = "비밀번호가 일치하지 않습니다.";
        passwordMatchWarning.style.color = "red";
    } else {
        passwordMatchWarning.textContent = "";
    }
}

function validateEmail() {
    const emailInput = document.getElementById("userEmail");
    const emailWarning = document.getElementById("emailWarning");
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!emailInput || !emailWarning) return;

    if (!emailPattern.test(emailInput.value)) {
        emailWarning.textContent = "올바른 이메일 형식이 아닙니다.";
        emailWarning.style.color = "red";
    } else {
        emailWarning.textContent = "";
    }
}

function validatePhoneNumber() {
    let phone1 = document.getElementById("phone1")?.value;
    let phone2 = document.getElementById("phone2")?.value;
    let phone3 = document.getElementById("phone3")?.value;
    let userPhone = phone1 + "-" + phone2 + "-" + phone3;

    const phoneWarning = document.getElementById("phoneWarning");
    const phonePattern = /^\d{3}-\d{3,4}-\d{4}$/;

    if (!phoneWarning) return;

    if (!phonePattern.test(userPhone)) {
        phoneWarning.textContent = "올바른 전화번호 형식이 아닙니다.";
        phoneWarning.style.color = "red";
    } else {
        phoneWarning.textContent = "";
    }
}

// ==== 비밀번호 변경 등에서 재사용할 수 있도록 export 함수 정의 ====

export function validatePasswordForUpdate(password) {
    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/;
    return passwordPattern.test(password);
}

export function isPasswordMatch(pw1, pw2) {
    return pw1 === pw2;
}
