<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>아이템 목록</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
	<link rel="stylesheet" type="text/css" href="/css/styleTemp.css">
	<link rel="stylesheet" type="text/css" href="/css/styleWeather.css">
	<link rel="stylesheet" type="text/css" href="/css/styleMainPage.css">
    <script src="/js/weather.js"></script> 
</head>
<body>
	<div> 
	    <div class="weather-container"></div>
	    <div id="weatherUpdateTime"></div>
	    <div id="weatherError"></div>
	    <div id="weatherItem">
		    <strong>추천 거래 (3km 이내)</strong>
			<div id="item-list" class="itemOption" style="min-height: 100px; justify-content: center;"><!-- 아이템 리스트 --></div>
		</div>
	</div>
    <div class="body-container">
        <jsp:include page="item.jsp" />
    </div>
	<jsp:include page="../footer.jsp" />
    <jsp:include page="../sidebar.jsp" />
    <jsp:include page="../main/bottom_nav.jsp" />
</body>
<script src="/js/common.js"></script>
<script src="/js/recent/config.js"></script>
<script src="/js/recent/getRecent_sidebar.js"></script>
<script>
    function sortItems() {  
        let sortOrder = document.getElementById("sortOrder");
        let selectedValue = sortOrder.value;
        
        sessionStorage.setItem("sortOrder", selectedValue);
        
        window.location.href = `?sortOrder=` + selectedValue;
    };     
</script>
</html>
