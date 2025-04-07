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
</head>
<body>	
	<jsp:include page="categoryMenu.jsp" />
	<div>
        <p> 
        	<span id="searchPath">${searchPath }</span>
            <span id="gender" style="display: none;">${gender}</span> 
            <span id="clothesType" style="display: none;">${ct}</span>
            <span id="clothesTypeDetail" style="display: none;">${cd}</span>
        </p>
    </div>
	<div class="body-container">
		<jsp:include page="item.jsp"/>
	</div>
	<jsp:include page="../sidebar.jsp" />
	<jsp:include page="../footer.jsp"/>
	<jsp:include page="../main/bottom_nav.jsp" />
</body>
<script src="/js/common.js"></script>
<script>	
function sortItems() {
	let gender = document.getElementById("gender").textContent;
	let clothesType = document.getElementById("clothesType").textContent;  
	let clothesTypeDetail = document.getElementById("clothesTypeDetail").textContent;  
	
	let sortOrder = document.getElementById("sortOrder");
	let selectedValue = sortOrder.value;
	
	sessionStorage.setItem("sortOrder", selectedValue);
	
	// 새로운 URL 형식에 맞게 수정
	window.location.href = `/category/${gender}?ct=` + clothesType
						+ `&cd=` + clothesTypeDetail 
						+ `&sortOrder=` + selectedValue;
};
</script>
</html>
