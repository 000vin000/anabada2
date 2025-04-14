<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>문의사항 상세보기</title>
    <link rel="stylesheet" type="text/css" href="/css/question.css" />
</head>
<body>
    <h2>문의사항 상세보기</h2>
	
    <table border="1">
        <tr>
            <th>번호</th>
            <td>${param.index}</td>
        </tr>
        <tr>
            <th>제목</th>
            <td>${question.questionTitle}</td>
        </tr>
        <tr>
            <th>내용</th>
            <td>${question.questionContent}</td>
        </tr>
    <tr>
    <th>답변</th>
    <td>
        <c:if test="${not empty question.answers}">
            <c:forEach var="answer" items="${question.answers}" varStatus="status">
                <p>${answer.answerContent}</p>
            </c:forEach>
        </c:if>
        <c:if test="${empty question.answers}">
            답변 대기 중입니다.
        </c:if>
    </td>
</tr>


    </table>

    <br>
    <a href="/question/mypage/myQuestions">← 목록으로 돌아가기</a>
</body>
</html>
