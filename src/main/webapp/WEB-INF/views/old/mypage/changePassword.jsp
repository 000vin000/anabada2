<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ include file="/WEB-INF/views/header.jsp" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>비밀번호 변경</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/js/changePassword.js"></script>
</head>
<body>
    <c:if test="${not empty successMessage}">
        <script>
            alert("${successMessage}");
        </script>
    </c:if>
    <div class="deactivate-container">
        <ul class="breadcrumb" id="breadcrumb">
            <li><a href="/">홈</a></li>
            <li><a href="/mypage">마이페이지</a></li>
            <li><a href="/mypage/updateinfo">회원정보 관리</a></li>
            <li><a href="/mypage/changePassword">비밀번호 변경</a></li>
        </ul>
        
        <h1>회원정보 관리</h1>
        <a href="/mypage/updateinfo" class="update-info-link">회원 정보</a>    
        <a href="/mypage/changePassword" class="change-password-link">비밀번호 변경</a>
        <a href="/mypage/deactivate" class="withdraw-link">회원 탈퇴</a>
        
        <div class="change-password-container">
            <h2>비밀번호 변경</h2>
            
            <form:form action="/mypage/changePassword" method="post" modelAttribute="passwordChangeForm">
                <div>
                    <label for="currentPassword">현재 비밀번호:</label>
                    <form:password path="currentPassword" id="currentPassword" required="true" />
                    <form:errors path="currentPassword" cssClass="error-message" />
                </div>

                <div>
                    <label for="newPassword">새 비밀번호:</label>
                    <form:password path="newPassword" id="newPassword" required="true" />
                    <span id="passwordRuleResult"></span>
                    <form:errors path="newPassword" cssClass="error-message" />
                </div>

                <div>
                    <label for="confirmNewPassword">새 비밀번호 확인:</label>
                    <form:password path="confirmNewPassword" id="confirmNewPassword" required="true" />
                    <span id="passwordMatchResult"></span>
                    <form:errors path="confirmNewPassword" cssClass="error-message" />
                </div>
                
                <button type="submit">비밀번호 변경</button>
            </form:form>
        </div>
    </div>
    
    <%@ include file="../footer.jsp" %>
</body>
</html>
