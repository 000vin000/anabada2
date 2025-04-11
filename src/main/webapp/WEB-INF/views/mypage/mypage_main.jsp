<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ANABADA</title>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
	<link rel="stylesheet" type="text/css" href="/css/styleTemp.css">
	<link rel="stylesheet" type="text/css" href="/css/styleCoinView.css">
	<script type="module" src="/js/user/common/fetchWithAuth.js"></script> 
</head>
<body>
<div class="body-container">
<!-- 사용자 정보 -->
<section class="user-info">
    <h3>사용자 정보</h3>
    <a id="updateInfoBtn" href="#">회원정보 수정</a>
    <a id="withdrawBtn" href="#">회원 탈퇴</a>
    <div id="user-info"></div>

    <!-- 회원탈퇴 버튼 추가 -->
    <div style="margin-top: 20px;">
        <button id="withdrawPageBtn">회원 탈퇴</button>
    </div>
</section>
	
	<!-- 보유 재화 정보 -->
	<section>
		<jsp:include page="../mypage/coinView.jsp" />
	</section>
	<div>
		<a href="/question/write">고객센터</a><br>
		<a href="/question/mypage/myQuestions">1:1 문의 내역</a><br>
		<a href="#">상품 문의 내역</a><br>
		<a href="/notice/list">공지 사항</a><br>
	</div>
</div>
<jsp:include page="../footer.jsp" />
<jsp:include page="../main/bottom_nav.jsp" />
<script type="module" src="/js/user/pin/goToUpdateInfo.js"></script>
</body>
</html>
