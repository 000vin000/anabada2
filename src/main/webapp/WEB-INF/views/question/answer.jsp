<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>문의사항 답변</title>
    <link rel="stylesheet" type="text/css" href="/css/question.css" />
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
                <button type="submit" class="btn btn-primary">답변</button>
                <a href="${pageContext.request.contextPath}/admin/management" class="btn btn-secondary">취소</a>
            </div>
            <input type="hidden" id="questionNo" name="questionNo" value="${question.questionNo}" />
        </form>
    </div>
    <script type="module" src="/js/answer.js"></script>
</body>
</html>
