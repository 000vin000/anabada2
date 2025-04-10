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
	<script type="module" src="/js/questionUpdate.js"></script>
</body>
</html>
