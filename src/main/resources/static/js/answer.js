import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('answerForm');

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const questionNo = document.getElementById('questionNo').value;
        const answerContent = document.getElementById('answerContent').value;

        const answerData = { answerContent: answerContent };

        fetchWithAuth(`/api/question/answer/${questionNo}`, {
            method: 'POST',
            body: JSON.stringify(answerData),
            headers: { 'Content-Type': 'application/json' }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 401) {
                alert('로그인이 필요합니다.');
                window.location.href = '/login';
                throw new Error('Unauthorized');
            } else {
                alert('답변 등록 실패. 다시 시도해 주세요.');
                return response.json();
            }
        })
        .then(data => {
            if (data && data.message) {
                alert(data.message);
                window.location.href = '/admin/management';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('답변 등록 중 오류가 발생했습니다.');
        });
    });
});
