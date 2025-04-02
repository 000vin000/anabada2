<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>문의사항 답변</title>
</head>
<body>
    <div class="container">
        <h2>문의사항 답변</h2>

        <!-- 문의 내용 표시 -->
        <c:if test="${not empty question}">
            <h3>문의 제목: ${question.questionTitle}</h3>
            <p><strong>문의 내용:</strong> ${question.questionContent}</p>
        </c:if>

        <!-- 답변 작성 폼 -->
        <form id="answerForm" method="post">
            <div class="form-group">
                <label for="answerContent">답변 내용</label>
                <textarea id="answerContent" name="answerContent" rows="5" required></textarea>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">답변하기</button>
                <a href="${pageContext.request.contextPath}/admin/management" class="btn btn-secondary">취소</a>
            </div>
            <input type="hidden" id="questionNo" name="questionNo" value="${question.questionNo}" />
        </form>
    </div>

    <script type="module">
        import { fetchWithAuth } from '/js/user/fetchWithAuth.js';

        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('answerForm');
            
            form.addEventListener('submit', function(event) {
                event.preventDefault(); // 기본 폼 제출 방지

                const questionNo = document.getElementById('questionNo').value;
                const answerContent = document.getElementById('answerContent').value;

                const answerData = { answerContent: answerContent };

                // POST 요청을 통해 답변 등록
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
                        alert(data.message); // 서버에서 반환한 메시지 출력
                        window.location.href = '/admin/management'; // 성공 후 이동
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('답변 등록 중 오류가 발생했습니다.');
                });
            });
        });
    </script>
</body>
</html>
