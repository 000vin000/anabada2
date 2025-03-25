<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>채팅방 생성</title>
</head>
<body>
    <h1>새 채팅방 생성</h1>
    <form action="/chat/createRoom" method="post">
        <label for="itemTitle">상품 제목:</label>
        <input type="text" id="itemTitle" name="itemTitle" required><br>
        
        <label for="sellerId">판매자 ID:</label>
        <input type="number" id="sellerId" name="sellerId" required><br>
        
        <label for="buyerId">구매자 ID:</label>
        <input type="number" id="buyerId" name="buyerId" required><br>
        
        <label for="itemNo">상품 번호:</label>
        <input type="number" id="itemNo" name="itemNo" required><br>
        
        <button type="submit">채팅방 생성</button>
    </form>
</body>
</html>
