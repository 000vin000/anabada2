// js/question/my_questions.js
import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';

// 페이지 로드 후 문의사항 가져오기
document.addEventListener('DOMContentLoaded', fetchQuestions);

// 문의사항을 서버에서 가져오기
function fetchQuestions() {
    fetchWithAuth('/api/question/mypage/myQuestions', { method: 'GET' })
        .then(response => response.json())
        .then(renderQuestions)
        .catch(error => {
            console.error('Error:', error);
            alert('문의사항을 불러오는 중 오류가 발생했습니다.');
        });
}

// 문의사항을 테이블에 렌더링
function renderQuestions(questions) {
    if (questions.length === 0) {
        alert('문의사항이 없습니다.');
        return;
    }

    const tableBody = document.querySelector('#questionsTable');
    tableBody.innerHTML = '';

    questions.forEach(question => {
        const row = document.createElement('tr');
        row.id = `question-${question.questionNo}`;

        row.innerHTML = `
            <td>${question.questionNo}</td>
            <td>${question.questionTitle}</td>
            <td>${question.questionContent}</td>
			<td>
			  ${question.answers && question.answers.length > 0
			    ? question.answers[0].answerContent
			    : '답변 대기 중'}
			</td>
            <td>
                <a href="/question/edit/${question.questionNo}">수정</a>
                <button data-question-no="${question.questionNo}">삭제</button>
            </td>
        `;

        const deleteButton = row.querySelector('button');
        deleteButton.addEventListener('click', () => {
            deleteQuestion(question.questionNo);
        });

        tableBody.appendChild(row);
    });
}

// 삭제 함수
function deleteQuestion(questionNo) {
    if (confirm("정말 삭제하시겠습니까?")) {
        const deleteUrl = '/api/question/delete/' + questionNo;

        fetchWithAuth(deleteUrl, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert('삭제되었습니다.');
                document.getElementById(`question-${questionNo}`).remove();
            } else if (response.status === 401) {
                alert('로그인이 만료되었습니다. 다시 로그인해주세요.');
                window.location.href = '/login';
            } else if (response.status === 403) {
                alert('삭제 권한이 없습니다.');
            } else if (response.status === 404) {
                alert('해당 문의를 찾을 수 없습니다.');
            } else {
                alert('삭제 실패. 다시 시도해 주세요.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('삭제 중 오류가 발생했습니다.');
        });
    }
}
