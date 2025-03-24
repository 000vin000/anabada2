<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ANABADA</title>
</head>
<body>
<div class="body-container">
	<!-- 판매자 정보 -->
	<div id="seller-info" class="seller-box">
	    <h2 id="seller-name">${seller.user.userName}</h2>
	    <p id="seller-description"></p>
	    <div>
	    	판매자 평균 별점
	    	판매자 등급(판매물품갯수)
	    </div>
	</div>
	
    <!-- 탭 메뉴 -->
    <div class="tabs">
        <button class="tab-button active" onclick="showTab('selling')">판매 중</button>
        <button class="tab-button" onclick="showTab('sold')">판매 완료</button>
    </div>

    <!-- 판매 중인 물품 목록 -->
    <div id="selling-items" class="tab-content active">
        <h3>판매 중인 상품</h3>
        <div id="items-list" class="items-grid"></div>
    </div>

    <!-- 판매 완료된 물품 목록 -->
    <div id="sold-items" class="tab-content">
        <h3>판매 완료된 상품 및 거래 후기</h3>
        <div id="sold-list" class="items-grid"></div>
    </div>
</div>
</body>
</html>