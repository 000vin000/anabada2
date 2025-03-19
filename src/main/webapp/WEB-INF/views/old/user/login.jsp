<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ include file="/WEB-INF/views/header.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>

<div class="container">
    <div class="login-box">
        <h2 id="login-label">로그인</h2>
        <form action="<c:url value='/user/login'/>" method="post">
            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>

            <!-- 성공 메시지 처리 -->
            <c:if test="${not empty successMessage}">
                <script>
                    alert("${successMessage}");
                </script>
            </c:if>

            <div class="form-group">
                <label for="userId">아이디</label>
                <input type="text" id="userId" name="userId" required />
            </div>

            <div class="form-group">
                <label for="userPw">비밀번호</label>
                <input type="password" id="userPw" name="userPw" required />
            </div>

            <button type="submit" id="loginBtn">로그인</button>
        </form>
        <div class="signup-link">
            <a href="<c:url value='/user/join'/>">회원가입</a>
        </div>
    </div>
</div>

</body>
</html>

<%@ include file="../footer.jsp" %>
