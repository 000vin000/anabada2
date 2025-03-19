<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>이메일 인증</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
    <div class="email-verification-container">
        <h2>이메일 인증</h2>
        <form id="emailForm">
            <div class="input-container">
                <input type="email" id="email" name="email" placeholder="이메일 입력" required>
            </div>
            <div class="input-container">
                <button type="button" onclick="sendVerificationEmail()">인증 코드 받기</button>
            </div>
        </form>
        <div id="verificationSection">
            <div class="input-container">
                <input type="text" id="verificationCode" placeholder="인증 코드 입력" required>
            </div>
            <div class="input-container">
                <button type="button" onclick="verifyEmail()">인증하기</button>
            </div>
        </div>
    </div>

    <script src="/js/emailVerification.js"></script>
</body>
</html>
<%@ include file="../footer.jsp" %>
