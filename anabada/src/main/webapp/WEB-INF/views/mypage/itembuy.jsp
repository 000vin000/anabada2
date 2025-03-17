<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>구매 현황</title>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
	<div class="body-container">
	    <!-- 브레드 크럼 -->
	    <ul class="breadcrumb" id="breadcrumb">
			<li><a href="/">홈</a></li>
	    	<li><a href="/mypage">마이페이지</a></li>
	    	<li><a href="/mypage/itembuy">구매 현황</a></li>
	    </ul>
		<h1>구매 현황</h1>
		<table class="tableFavor">
		    <thead>
		        <tr>
		        	<th>이미지</th>
		            <th>상품명</th>
		            <th>상태</th>
		            <th>입찰가</th>
		            <th>현재가</th>
		            <th>입찰시간</th>
		            <th>마감시간</th>
		        </tr>
		    </thead>
		    <tbody>
		        <c:forEach var="item" items="${buylist}">
		            <tr>
		                <td><a href="/item/detail/${item.itemNo}"><img src="data:image/png;base64,${item.getImageBase64()}"></a></td>
		                <td><a href="/item/detail/${item.itemNo}" style="text-decoration: none; color: inherit;">${item.itemName}</a></td>
		                <td>${item.getItemAuctionName()}</td>
		                <td>${item.bidPrice}원</td>
		                <td>${item.itemPrice}원</td>
		                <td>${item.getFormattedBidTime()}</td>
		                <td>${item.getFormattedItemEnd()}</td>
		            </tr>
		        </c:forEach>
	    	</tbody>
		</table>
	</div>
	
	<jsp:include page="../sidebar.jsp" />
	<jsp:include page="../footer.jsp" />
</body>
<script src="/js/todaypick.js"></script>
</html>