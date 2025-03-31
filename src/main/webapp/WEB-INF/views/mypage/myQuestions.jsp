<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 문의사항 목록</title>

    <script type="module">
    import { fetchWithAuth } from '/js/user/fetchWithAuth.js';

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
        console.log(questions);  // 데이터 확인

        if (questions.length === 0) {
            alert('문의사항이 없습니다.');
            return;
        }

        const tableBody = document.querySelector('#questionsTable');
        tableBody.innerHTML = ''; // 테이블 비우기

        questions.forEach(question => {
            const row = document.createElement('tr');
            row.id = `question-${question.questionNo}`;  // 각 행에 고유 ID 추가

            const cell1 = document.createElement('td');
            cell1.textContent = question.questionNo;
            row.appendChild(cell1);

            const cell2 = document.createElement('td');
            cell2.textContent = question.questionTitle;
            row.appendChild(cell2);

            const cell3 = document.createElement('td');
            cell3.textContent = question.questionContent;
            row.appendChild(cell3);

            const cell4 = document.createElement('td');
            cell4.textContent = question.questionStatus;
            row.appendChild(cell4);

            const cell5 = document.createElement('td');

            // 수정 링크
            const editLink = document.createElement('a');
            editLink.href = '/question/edit/' + question.questionNo; 
            editLink.textContent = '수정';
            cell5.appendChild(editLink);

            // 삭제 버튼
            const deleteButton = document.createElement('button');
            deleteButton.textContent = '삭제';
            deleteButton.setAttribute('data-question-no', question.questionNo);

            // 삭제 버튼 클릭 시 삭제 함수 호출
            deleteButton.addEventListener('click', function () {
                const questionNo = this.getAttribute('data-question-no');
                console.log('삭제하려는 questionNo:', questionNo);
                deleteQuestion(questionNo);
            });

            cell5.appendChild(deleteButton);
            row.appendChild(cell5);

            tableBody.appendChild(row);
        });
    }

    // 삭제 함수 (AJAX를 통해 삭제 처리)
    function deleteQuestion(questionNo) {
        // 삭제 확인
        if (confirm("정말 삭제하시겠습니까?")) {
            const deleteUrl = '/api/question/delete/' + questionNo;

            // fetchWithAuth 함수 사용하여 DELETE 요청 보내기
            fetchWithAuth(deleteUrl, {
                method: 'DELETE',  // DELETE 메소드 사용
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`,  // 인증 헤더에 토큰 포함
                    'Content-Type': 'application/json',  // 요청 헤더
                }
            })
            .then(response => {
                if (response.ok) {
                    alert('삭제되었습니다.');
                    document.getElementById(`question-${questionNo}`).remove();  // 삭제된 행 제거
                } else if (response.status === 401) {
                    alert('로그인이 만료되었습니다. 다시 로그인해주세요.');
                    window.location.href = '/login';  // 로그인 페이지로 리다이렉트
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
</script>


</head>
<body>

<h2>내 문의사항 목록</h2>

<table border="1">
    <thead>
        <tr>
            <th>번호</th>
            <th>문의 제목</th>
            <th>문의 내용</th>
            <th>상태</th>
            <th>작업</th>
        </tr>
    </thead>
    <tbody id="questionsTable">
        <!-- 여기에 JavaScript로 문의 목록을 채울 것 -->
    </tbody>
</table>

</body>
</html>
