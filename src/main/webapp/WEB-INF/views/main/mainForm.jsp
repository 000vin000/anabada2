<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>아이템 목록</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"> <%-- 사이드바 css --%>
    <script src="/js/weather.js"></script> 
</head>
<body>
	<jsp:include page="categoryMenu.jsp" />
	<div style="background-color: #FFF0F0;">
	    <div class="weather-container"></div>
		<div id="item-list"><!-- 아이템 리스트 --></div>
	</div>
    <div class="body-container">
        <jsp:include page="item.jsp" />
    </div>
    <jsp:include page="../sidebar.jsp" />
    <jsp:include page="../footer.jsp" /> 
</body>
<script src="/js/common.js"></script>
<script>
    function sortItems() {  
        let sortOrder = document.getElementById("sortOrder");
        let selectedValue = sortOrder.value;
        
        sessionStorage.setItem("sortOrder", selectedValue);
        
        window.location.href = `?sortOrder=` + selectedValue;
    };     
    document.getElementById("weatherButton").addEventListener("click", function() {
        window.location.href = "/weather"; // '/weather' 경로로 이동
    });
</script>
</html>
