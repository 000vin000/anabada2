<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>채팅방</title>
    <style>
        /* 채팅 박스 스타일 */
        #chat-box {
            border: 1px solid #ccc;
            height: 300px;
            overflow-y: scroll;
            padding: 10px;
            display: flex;
            flex-direction: column;
        }

        /* 각 메시지 스타일 */
        .message {
            margin-bottom: 10px;
            display: flex;
            align-items: center;
        }

        /* 보낸 메시지 스타일 (오른쪽 정렬) */
        .sent {
            justify-content: flex-end;
        }

        /* 받은 메시지 스타일 (왼쪽 정렬) */
        .received {
            justify-content: flex-start;
        }

        /* 메시지 텍스트 스타일 */
        .message p {
            margin: 0;
            padding: 5px 10px;
            border-radius: 10px;
        }

        /* 보낸 메시지 박스 색상 */
        .sent p {
            background-color: #a1f7d1;  
        }

        /* 받은 메시지 박스 색상 */
        .received p {
            background-color: #f1f1f1;
        }

        /* 시간 스타일 */
        .timestamp {
            font-size: 0.8em;
            color: gray;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <h1>채팅방 - Room No: ${roomNo}</h1>
    <div id="chat-box">
        <!-- 반복문으로 메시지와 시간 표시 -->
        <c:forEach var="message" items="${messages}">
            <div class="message ${message.sender == user ? 'sent' : 'received'}">
                <p>${message.msgContent}</p>
                <!-- 시간 표시 -->
                <span class="timestamp">
                    <fmt:formatDate value="${message.msgDate}" pattern="yyyy-MM-dd HH:mm" />
                </span>
            </div>
        </c:forEach>
    </div>
    <input type="text" id="message" placeholder="메시지를 입력하세요" />
    <button onclick="sendMessage()">전송</button>

    <script>
        const roomNo = "${roomNo}";
        const ws = new WebSocket(`ws://localhost:8080/chat/${roomNo}`);

        // WebSocket 메시지를 받으면 채팅박스에 표시
        ws.onmessage = function(event) {
            const chatBox = document.getElementById("chat-box");
            const messageData = event.data.split(","); // 시간,  메시지 구분

            // 받은 메시지
            const messageDiv = document.createElement("div");
            messageDiv.classList.add("message", "received");
            const messageText = document.createElement("p");
            messageText.textContent = messageData[0]; 
            messageDiv.appendChild(messageText);
            
            const timestampSpan = document.createElement("span");
            timestampSpan.classList.add("timestamp");

            const timestamp = new Date(messageData[1]);
            const formattedTime = timestamp.getFullYear() + "-" + 
                                  String(timestamp.getMonth() + 1).padStart(2, '0') + "-" + 
                                  String(timestamp.getDate()).padStart(2, '0') + " " + 
                                  String(timestamp.getHours()).padStart(2, '0') + ":" + 
                                  String(timestamp.getMinutes()).padStart(2, '0');

            timestampSpan.textContent = formattedTime; // 시간 표시
            messageDiv.appendChild(timestampSpan);
            
            chatBox.appendChild(messageDiv);

            // 스크롤을 자동으로 맨 아래로 내리기
            chatBox.scrollTop = chatBox.scrollHeight;
        };

        // 메시지 보내기
        function sendMessage() {
            const message = document.getElementById("message").value;
            if (message.trim() !== "") {
                const chatBox = document.getElementById("chat-box");

                // 보낸 메시지 화면에 추가
                const messageDiv = document.createElement("div");
                messageDiv.classList.add("message", "sent");
                const messageText = document.createElement("p");
                messageText.textContent = message;
                messageDiv.appendChild(messageText);

                const timestampSpan = document.createElement("span");
                timestampSpan.classList.add("timestamp");
                const now = new Date();
                const formattedTime = now.getFullYear() + "-" + 
                                      String(now.getMonth() + 1).padStart(2, '0') + "-" + 
                                      String(now.getDate()).padStart(2, '0') + " " + 
                                      String(now.getHours()).padStart(2, '0') + ":" + 
                                      String(now.getMinutes()).padStart(2, '0');
                timestampSpan.textContent = formattedTime;

                messageDiv.appendChild(timestampSpan);

                chatBox.appendChild(messageDiv);

                // 입력란
                document.getElementById("message").value = "";

                // 서버로 메시지 전송
                const messageData = message + "," + formattedTime;
                ws.send(messageData); // 메시지, 시간

                // 스크롤 자동으로 맨 아래로 내리기
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        }

        // 엔터 키로 메시지 전송
        document.getElementById("message").addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                sendMessage();
            }
        });
    </script>
</body>
</html>
