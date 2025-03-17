function sendVerificationEmail() {
    var email = document.getElementById('email').value;
    fetch('/email/send-verification', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'email=' + encodeURIComponent(email)
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message);
        if (data.message === "인증 코드가 이메일로 전송되었습니다.") {
            document.getElementById('verificationSection').style.display = 'block';
        }
    });
}

function verifyEmail() {
    var code = document.getElementById('verificationCode').value;
    fetch('/email/verify', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'code=' + encodeURIComponent(code)
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message);
        if (data.status === "success") {
            window.location.href = '/user/join';
        }
    });
}
