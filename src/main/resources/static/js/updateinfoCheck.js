// 주소 검색 기능
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = '';
            var extraAddr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if(data.userSelectedType === 'R'){
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                addr += extraAddr;
            }

            document.getElementById("userAdd").value = data.zonecode + ' ' + addr;
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

// 폼 제출 전 상세주소를 userAdd에 추가
document.querySelector('form').onsubmit = function() {
    var userAdd = document.getElementById('userAdd');
    var detailAddress = document.getElementById('detailAddress');
    if (detailAddress.value) {
        userAdd.value += ' ' + detailAddress.value;
    }
    return true;
};

// 닉네임 중복 체크 (입력 즉시)
$('#userNick').on('input', function() {
    checkDuplicate('userNick', $(this).val(), '#userNickCheckResult');
});

// 전화번호 중복 체크 (입력 즉시)
$('#userPhone1, #userPhone2, #userPhone3').on('input', function() {
    const phone1 = $('#userPhone1').val();
    const phone2 = $('#userPhone2').val();
    const phone3 = $('#userPhone3').val();

    if (phone1.length === 3 && (phone2.length === 3 || phone2.length === 4) && phone3.length === 4) {
        const fullPhone = `${phone1}${phone2}${phone3}`;
        checkDuplicate('userPhone', fullPhone, '#userPhoneCheckResult');
    } else {
        $('#userPhoneCheckResult').text('');
    }
});

// 비밀번호 관련 함수 및 이벤트 리스너 제거
// $('#userPw').on('input', function() { ... }); 삭제
// $('#userPw2').on('input', function() { ... }); 삭제
// function checkPasswordRule(password) { ... } 삭제
// function checkPasswordMatch() { ... } 삭제

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
        $('#passwordRuleResult').text('');
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
            $('#passwordMatchResult').text('');
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