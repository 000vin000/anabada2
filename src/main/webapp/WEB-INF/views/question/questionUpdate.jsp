<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>문의사항 수정</title>
</head>
<body>
    <div class="container">
        <h2>문의사항 수정</h2>

        <!-- 수정 폼 -->
        <form id="editQuestionForm" method="post">
            <div class="form-group">
                <label for="questionTitle">제목</label>
                <input type="text" id="questionTitle" name="questionTitle" value="${question.questionTitle}" required>
            </div>

            <div class="form-group">
                <label for="questionContent">내용</label>
                <textarea id="questionContent" name="questionContent" required>${question.questionContent}</textarea>
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary">수정하기</button>
                <a href="${pageContext.request.contextPath}/question/mypage/myQuestions" class="btn btn-secondary">취소</a>
            </div>
            <input type="hidden" id="questionNo" name="questionNo" value="${question.questionNo}" />
        </form>
    </div>

    <script type="module">
   import { fetchWithAuth } from '/js/user/fetchWithAuth.js';  // 파일 경로는 실제 파일 위치에 맞게 설정

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

        // PUT 요청을 통해 수정 요청
        fetchWithAuth(`/api/question/edit/${questionNo}`, {
            method: 'PUT',  // PUT 요청으로 수정
            body: JSON.stringify(updatedQuestion),  // 수정된 문의사항 내용
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 401) {
                alert('로그인이 필요합니다. 토큰이 만료되었을 수 있습니다.');
                window.location.href = '/login';  // 로그인 페이지로 리다이렉트
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
                alert(data.message);  // 서버에서 반환한 메시지 출력
                window.location.href = '/question/mypage/myQuestions';  // 수정 후 목록 페이지로 리다이렉트
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('수정 중 오류가 발생했습니다.');
        });
    });
});

    </script>

</body>
</html>
