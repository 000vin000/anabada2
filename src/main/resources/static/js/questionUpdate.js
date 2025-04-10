import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js'; // 실제 경로에 맞게 조정하세요

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('editQuestionForm');

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const questionNo = document.getElementById('questionNo').value;
        const questionTitle = document.getElementById('questionTitle').value;
        const questionContent = document.getElementById('questionContent').value;

        const updatedQuestion = {
            questionNo: questionNo,
            questionTitle: questionTitle,
            questionContent: questionContent
        };

        fetchWithAuth(`/api/question/edit/${questionNo}`, {
            method: 'PUT',
            body: JSON.stringify(updatedQuestion),
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 401) {
                alert('로그인이 필요합니다. 토큰이 만료되었을 수 있습니다.');
                window.location.href = '/login';
                throw new Error('Unauthorized');
            } else if (response.status === 403) {
                alert('수정 권한이 없습니다.');
                throw new Error('Forbidden');
            } else {
                alert('수정 실패. 서버에서 오류가 발생했습니다. 다시 시도해 주세요.');
                return response.json();
            }
        })
        .then(data => {
            if (data && data.message) {
                alert(data.message);
                window.location.href = '/question/mypage/myQuestions';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('수정 중 오류가 발생했습니다.');
        });
    });
});
