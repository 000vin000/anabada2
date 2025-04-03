<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ANABADA</title>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
	<link rel="stylesheet" type="text/css" href="/css/favorpageStyle.css">
	<style type="text/css">
		.tabs {
		    display: flex;
		    border-bottom: 2px solid #ddd;
		}
		
		.tab {
		    padding: 10px 20px;
		    cursor: pointer;
		}
		
		.tab.active {
		    font-weight: bold;
		    border-bottom: 3px solid blue;
		}
		
		.content {
		    display: none;
		    margin-top: 20px;
		}
		
		.content.active {
		    display: block;
		}
	</style>
</head>
<body>
<div class="body-container">
    <!-- 브레드 크럼 -->
    <ul class="breadcrumb" id="breadcrumb">
		<li><a href="/">홈</a></li>
    	<li><a href="/mypage">마이페이지</a></li>
    	<li><a href="/mypage/favor">관심물품</a></li>
    </ul>
	<h1>관심 물품</h1>
	<div class="tabs">
	    <div class="tab active" onclick="showTab('items')">관심물품</div>
	    <div class="tab" onclick="showTab('sellers')">관심판매자</div>
	</div>
	
	<div id="items" class="content active">
	    <ul id="itemList" class="favor-list"> </ul>
	</div>
	
	<div id="sellers" class="content">
	    <ul id="sellerList" class="favor-list"> </ul>
	</div>
</div>
	<jsp:include page="../sidebar.jsp" />
	<jsp:include page="../footer.jsp" />
</body>
<script src="/js/recent/config.js"></script>
<script src="/js/recent/getRecent_sidebar.js"></script>
<script src="/js/favor/favorpage.js" defer></script>
</html>