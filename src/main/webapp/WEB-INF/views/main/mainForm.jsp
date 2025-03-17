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
</head>
<body>
	<div class="body-container">
    	<jsp:include page="item.jsp" />
    </div>
    <jsp:include page="../sidebar.jsp" />
    <jsp:include page="../footer.jsp" /> 
</body>
<script src="/js/common.js"></script>
<script src="/js/todaypick.js"></script>
<script>
        function sortItems() {  
            let sortOrder = document.getElementById("sortOrder");
            let selectedValue = sortOrder.value;
          	
            sessionStorage.setItem("sortOrder", selectedValue);
            
			window.location.href = `?sortOrder=` + selectedValue;
        }     
</script>
</html>
