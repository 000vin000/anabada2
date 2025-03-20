/**
 * 
 */

document.addEventListener("DOMContentLoaded", function() {
    const passwordInput = document.getElementById("userPw");
    const confirmPasswordInput = document.getElementById("userPw2");
    const passwordMatchResult = document.getElementById("passwordMatchResult");
    const passwordRuleResult = document.getElementById("passwordRuleResult");

    // 비밀번호 유효성 검사 (숫자, 문자, 특수문자 포함 6자 이상)
    function validatePassword() {
        const password = passwordInput.value;
        const regex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$/;

        if (!regex.test(password)) {
            passwordRuleResult.textContent = "비밀번호는 숫자, 문자, 특수문자를 포함하여 6자 이상이어야 합니다.";
            passwordRuleResult.style.color = "red";
            return false;
        } else {
            passwordRuleResult.textContent = "사용 가능한 비밀번호입니다.";
            passwordRuleResult.style.color = "green";
            return true;
        }
    }

    // 비밀번호 일치 여부 확인
    function checkPasswordMatch() {
        if (passwordInput.value !== confirmPasswordInput.value) {
            passwordMatchResult.textContent = "비밀번호가 일치하지 않습니다.";
            passwordMatchResult.style.color = "red";
            return false;
        } else {
            passwordMatchResult.textContent = "비밀번호가 일치합니다.";
            passwordMatchResult.style.color = "green";
            return true;
        }
    }

    // 이벤트 리스너 추가
    passwordInput.addEventListener("input", validatePassword);
    confirmPasswordInput.addEventListener("input", checkPasswordMatch);
});

