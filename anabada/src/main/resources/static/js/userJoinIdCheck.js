$(document).ready(function() {
    // 아이디 중복 체크 (버튼 클릭 시)
    $('#checkDuplicateUserId').click(function() {
        checkDuplicate('userId', $('#userId').val(), '#userIdCheckResult');
    });

    // 닉네임 중복 체크 (입력 즉시)
    $('#userNick').on('input', function() {
        checkDuplicate('userNick', $(this).val(), '#userNickCheckResult');
    });

    // 이메일 중복 체크 (입력 즉시)
    $('#userEmail').on('input', function() {
        checkDuplicate('userEmail', $(this).val(), '#userEmailCheckResult');
    });

    // 전화번호 중복 체크 (입력 즉시)
    $('#userPhone1, #userPhone2, #userPhone3').on('input', function() {
        const phone1 = $('#userPhone1').val();
        const phone2 = $('#userPhone2').val();
        const phone3 = $('#userPhone3').val();

        // 전화번호가 모두 입력되었을 때만 확인
        if (phone1.length === 3 && (phone2.length === 3 || phone2.length === 4) && phone3.length === 4) {
            const fullPhone = `${phone1}${phone2}${phone3}`; // "-" 없이 조합
            checkDuplicate('userPhone', fullPhone, '#userPhoneCheckResult');
        } else {
            // 입력이 완성되지 않은 경우 메시지 숨김
            $('#userPhoneCheckResult').text('');
        }
    });

    // 비밀번호 규칙 체크 (입력 즉시)
    $('#userPw').on('input', function() {
        checkPasswordRule($(this).val());
        checkPasswordMatch();
    });

    // 비밀번호 확인 체크 (입력 즉시)
    $('#userPw2').on('input', function() {
        checkPasswordMatch();
    });

    // 중복 체크 함수
    function checkDuplicate(field, value, resultSelector) {
        if (!value) {
            showMessage(`${getFieldName(field)}를 입력해주세요.`, 'red', resultSelector);
            return;
        }

        $.ajax({
            url: `/user/check-duplicate/${field}`,
            type: 'GET',
            data: { value: value },
            success: function(response) {
                if (response.invalidFormat) {
                    showMessage('올바른 형식이 아닙니다.', 'red', resultSelector);
                } else if (response.isDuplicate) {
                    showMessage(`사용할 수 없는 ${getFieldName(field)}입니다.`, 'red', resultSelector);
                } else {
                    showMessage(`사용 가능한 ${getFieldName(field)}입니다.`, 'green', resultSelector);
                }
            },
            error: function() {
                showMessage('중복 확인 중 오류가 발생했습니다.', 'red', resultSelector);
            }
        });
    }

    // 비밀번호 규칙 체크 함수
    function checkPasswordRule(password) {
        const passwordRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\S+$).{6,}$/;
        if (!passwordRegex.test(password)) {
            showMessage('비밀번호는 특수문자, 문자, 숫자를 포함하여 6자 이상이어야 합니다.', 'red', '#passwordRuleResult');
        } else {
            $('#passwordRuleResult').text(''); // 올바른 형식이면 메시지 제거
        }
    }

    // 비밀번호 일치 확인 함수
    function checkPasswordMatch() {
        const password = $('#userPw').val();
        const confirmPassword = $('#userPw2').val();
        
        if (password === confirmPassword) {
            if (password !== '') {
                showMessage('비밀번호가 일치합니다.', 'green', '#passwordMatchResult');
            } else {
                $('#passwordMatchResult').text(''); // 둘 다 비어있으면 메시지 제거
            }
        } else {
            showMessage('비밀번호가 일치하지 않습니다.', 'red', '#passwordMatchResult');
        }
    }

    function showMessage(message, color, target) {
        $(target).text(message).css('color', color);
    }

    function getFieldName(field) {
        switch(field) {
            case 'userId': return '아이디';
            case 'userNick': return '닉네임';
            case 'userEmail': return '이메일';
            case 'userPhone': return '전화번호';
            default: return field;
        }
    }
});