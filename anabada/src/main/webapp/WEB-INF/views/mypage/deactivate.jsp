<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원탈퇴</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
    <div class="deactivate-container">
        <ul class="breadcrumb" id="breadcrumb">
            <li><a href="/">홈</a></li>
            <li><a href="/mypage">마이페이지</a></li>
            <li><a href="/mypage/updateinfo">회원정보 관리</a></li>
			<li><a href="/mypage/deactivate">회원탈퇴</a></li>
        </ul>

        <h1>회원정보 관리</h1>
    <a href="/mypage/updateinfo" class="update-info-link">회원 정보</a>    
    <a href="/mypage/changePassword" class="change-password-link">비밀번호 변경</a>
    <a href="/mypage/deactivate" class="withdraw-link">회원 탈퇴</a>

        <form action="/mypage/deactivate" method="post">
            <h2>회원 탈퇴</h2>
            <p> </p>
            
            <button type="submit">탈퇴하기</button>
        </form>
    </div>
</body>
</html>
<%@ include file="../footer.jsp" %>
