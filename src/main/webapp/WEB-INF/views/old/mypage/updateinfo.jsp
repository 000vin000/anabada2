<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원정보 수정</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="/js/updateinfoCheck.js"></script>
</head>
<body>
<c:if test="${not empty successMessage}">
    <script>
        alert("${successMessage}");
    </script>
</c:if>
<div class="update-info-container">
    <ul class="breadcrumb" id="breadcrumb">
        <li><a href="/">홈</a></li>
        <li><a href="/mypage">마이페이지</a></li>
        <li><a href="/mypage/updateinfo">회원정보 관리</a></li>
		<li><a href="/mypage/updateinfo">회원정보 수정</a></li>
    </ul>
    
    <h1>회원정보 관리</h1>
    <a href="/mypage/updateinfo" class="update-info-link">회원 정보</a>    
    <a href="/mypage/changePassword" class="change-password-link">비밀번호 변경</a>
    <a href="/mypage/deactivate" class="withdraw-link">회원 탈퇴</a>
    
    <c:if test="${not empty success}">
        <p style="color: green;">${success}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
    
    <form:form action="/mypage/updateinfo" method="post" modelAttribute="user">
        <h2>회원정보 수정</h2>
        
        <form:hidden path="userId" value="${user.userId}"/>
   		 <form:hidden path="userPw" value="${user.userPw}"/>  <!-- 기존 비밀번호를 숨김 필드로 추가 -->
        <div>
            <label for="userName">이름:</label>
            <form:input type="text" id="userName" path="userName" required="true" />
            <form:errors path="userName" cssClass="error-message" />
        </div>

        <div>
            <label for="userNick">닉네임:</label>
            <form:input type="text" id="userNick" path="userNick" required="true"/>
            <form:errors path="userNick" cssClass="error-message" />
        </div>

        <div>
            <label for="userAdd">주소:</label>
            <form:input path="userAdd" id="userAdd" placeholder="주소" readonly="true" />
            <input type="button" onclick="execDaumPostcode()" value="주소 검색" />
            <input type="text" id="detailAddress" name="detailAddress" placeholder="상세주소" />
            <form:errors path="userAdd" cssClass="error-message" />
        </div>
        
        <div>
            <label for="userEmail">이메일:</label>
            <form:input type="email" path="userEmail" value="${userEmail}" readonly="true"/>
            <form:errors path="userEmail" cssClass="error-message" />
        </div>

        <div>
            <label for="userPhone">전화번호:</label>
            <input type="text" id="userPhone1" name="userPhone1" maxlength="3" size="3" value="${phone1}" required="true"/> -
            <input type="text" id="userPhone2" name="userPhone2" maxlength="4" size="4" value="${phone2}" required="true"/> -
            <input type="text" id="userPhone3" name="userPhone3" maxlength="4" size="4" value="${phone3}" required="true"/>
            <form:errors path="userPhone" cssClass="error-message" />
        </div>
        

        <button type="submit">수정하기</button>
    </form:form>
</div>
</body>
</html>
<%@ include file="../footer.jsp" %>
