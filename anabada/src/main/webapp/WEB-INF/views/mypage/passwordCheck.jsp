<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 확인</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
    <div class="password-check-container">
        <h1>비밀번호 확인</h1>
        <form action="/mypage/passwordcheck" method="post">
            <label for="userPw">비밀번호:</label>
            <input type="password" id="userPw" name="userPw" required>
            <input type="hidden" name="redirectTo" value="${param.redirectTo}">
            <button type="submit">확인</button>
        </form>
        <c:if test="${not empty error}">
            <p class="error-message">${error}</p>
        </c:if>
    </div>
</body>
</html>
<%@ include file="../footer.jsp" %>
