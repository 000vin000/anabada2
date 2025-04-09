import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('questionForm');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 제출 방지

        const formData = new FormData(form);
        const data = {
            questionTitle: formData.get('questionTitle'),
            questionContent: formData.get('questionContent'),
            qIsPrivate: formData.has('qIsPrivate') ? 'true' : 'false'
        };

        fetchWithAuth('/api/question/write', {
            method: 'POST',
            body: JSON.stringify(data),
        })
        .then(response => response.json())
        .then(result => {
            console.log(result);
            if (result.message) {
                alert(result.message || '문의 작성 완료');
                form.reset();
                window.location.href = '/question/mypage/myQuestions';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버와의 통신 오류');
        });
    });
});
