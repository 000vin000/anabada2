<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>채팅방</title>
    <style>
        /* 채팅 박스 스타일 */
        #chat-box {
            border: 1px solid #ccc;
            height: 400px;
            overflow-y: scroll;
            padding: 10px;
        }
        .message {
            margin-bottom: 10px;
        }
        .timestamp {
            font-size: 0.8em;
            color: gray;
        }
    </style>
</head>
<body>
    <h1>채팅방</h1>

    <!-- 채팅 메시지 목록 -->
    <div id="chat-box">
        <c:forEach var="message" items="${messages}">
            <div class="message">
                <p>${message.msgContent}</p>  <!-- ✅ message 필드명 수정 -->
                <span class="timestamp">
                    <fmt:formatDate value="${message.msgDate}" pattern="yyyy-MM-dd HH:mm" />
                </span>
            </div>
        </c:forEach>
    </div>

    <!-- 메시지 입력 및 전송 버튼 -->
    <input type="text" id="message-input" placeholder="메시지를 입력하세요">
    <button id="send-message">전송</button>
    <button id="leave-chat-room">나가기</button>

    <!-- 문의하기 버튼 -->
    <button id="inquiryBtn">문의하기</button>

    <!-- JavaScript 파일 연결 -->
    <script src="/js/chat/chatRoom.js"></script>
</body>
</html>
