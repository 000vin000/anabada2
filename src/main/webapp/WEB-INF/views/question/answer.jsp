<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>문의사항 답변하기</title>
</head>
<body>
<h2>문의사항 상세 보기</h2>

<!-- 문의사항 내용 표시 -->
<c:if test="${not empty question}">
    <h3>문의 제목: ${question.questionTitle}</h3>
    <p><strong>문의 내용:</strong> ${question.questionContent}</p>
</c:if>

<h2>답변 작성</h2>

<!-- 답변 작성 폼 -->
<form action="${pageContext.request.contextPath}/question/answer/${question.questionNo}" method="post">
    <label for="answerContent">답변 내용:</label><br/>
    <textarea id="answerContent" name="answerContent" rows="5" cols="40" placeholder="답변을 입력하세요..." required></textarea><br/><br/>

    <button type="submit">답변 달기</button>
</form>

</body>
</html>
