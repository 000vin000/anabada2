<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>채팅방</title>
    <link rel="stylesheet" type="text/css" href="/css/chatRoom.css">
</head>
<body>
    <h1>채팅방 - Room No: ${roomNo}</h1>

    <div id="chat-room" data-roomNo="${roomNo}" data-userNo="${loggedInUserNo}"></div>

    <div id="chat-box">

    </div>

    <textarea id="message-input" rows="2" placeholder="메시지를 입력하세요"></textarea>
    <button id="send-message">전송</button>
    <button id="leave-chat-room">나가기</button>

    <script>
        const loggedInUserNo = "${loggedInUserNo}"; // 숫자 그대로 넘김
    </script>
    <script src="/js/chat/chatRoom.js"></script>
</body>
</html>
