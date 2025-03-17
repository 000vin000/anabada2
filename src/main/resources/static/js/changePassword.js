$(document).ready(function() {
    $('#newPassword').on('input', function() {
        checkPasswordRule($(this).val());
        checkPasswordMatch();
    });

    $('#confirmNewPassword').on('input', function() {
        checkPasswordMatch();
    });

    function checkPasswordRule(password) {
        const passwordRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\S+$).{6,}$/;
        if (!passwordRegex.test(password)) {
            showMessage('비밀번호는 특수문자, 문자, 숫자를 포함하여 6자 이상이어야 합니다.', 'red', '#passwordRuleResult');
        } else {
            $('#passwordRuleResult').text('');
        }
    }

    function checkPasswordMatch() {
        const newPassword = $('#newPassword').val();
        const confirmNewPassword = $('#confirmNewPassword').val();
        
        if (newPassword === confirmNewPassword) {
            if (newPassword !== '') {
                showMessage('비밀번호가 일치합니다.', 'green', '#passwordMatchResult');
            } else {
                $('#passwordMatchResult').text('');
            }
        } else {
            showMessage('비밀번호가 일치하지 않습니다.', 'red', '#passwordMatchResult');
        }
    }

    function showMessage(message, color, target) {
        $(target).text(message).css('color', color);
    }
});
