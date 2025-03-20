<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 문의사항 목록</title>
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
    <tbody>
        <c:forEach var="question" items="${userQuestions}">
            <tr>
                <td>${question.questionNo}</td>
                <td>${question.questionTitle}</td>
                <td>${question.questionContent}</td>
                <td>${question.questionStatus}</td>
                <td><a href="/question/edit/${question.questionNo}">수정</a>
                	<form action="/question/delete/${question.questionNo}?from=mypage" method="post" style="display:inline;">
                    <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

</body>
</html>
