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
            background-color: #f9f9f9;
        }
        .message {
            margin-bottom: 10px;
            padding: 5px;
            border-radius: 5px;
        }
        .my-message {
            background-color: #d1e7ff;
            text-align: right;
        }
        .other-message {
            background-color: #e9ecef;
            text-align: left;
        }
        .timestamp {
            font-size: 0.8em;
            color: gray;
            display: block;
        }
    </style>
</head>
<body>
    <h1>채팅방</h1>

    <!-- 채팅방 ID 저장 (JavaScript에서 사용) -->
    <div id="chat-room" data-roomNo="${roomNo}"></div>

    <!-- 채팅 메시지 목록 -->
    <div id="chat-box">
        <c:choose>
            <c:when test="${not empty messages}">
                <c:forEach var="message" items="${messages}">
                    <div class="message ${message.senderNo == loggedInUserNo ? 'my-message' : 'other-message'}">
                        <p>${message.msgContent}</p>
                        
                            <span class="timestamp">${message.formattedMsgDate}</span>
                        
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>아직 메시지가 없습니다.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- 메시지 입력 및 전송 버튼 -->
    <input type="text" id="message-input" placeholder="메시지를 입력하세요">
    <button id="send-message">전송</button>
    <button id="leave-chat-room">나가기</button>

    <!-- JavaScript 파일 연결 -->
    <script src="/js/chat/chatRoom.js"></script>
</body>
</html>
