<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <title>ANABADA</title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<div class="body-container">
    <h2>최근 본</h2>
    <button id="delete-recent"><img src="/images/icon/xicon.png" width="32"></button>
    <div class="recent-items-container" id="recent-items" >
        <!-- 최근 본 아이템 목록이 여기에 추가됨 -->
    </div>
</div>
	<jsp:include page="../footer.jsp" />
</body>
<script src="/js/recent/config.js"></script>
<script src="/js/recent/getRecent_page.js"></script>
</html>