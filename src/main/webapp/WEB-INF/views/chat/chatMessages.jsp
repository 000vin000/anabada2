<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>채팅방</title>
    <style>
        #chat-box {
            border: 1px solid #ccc;
            height: 300px;
            overflow-y: scroll;
            padding: 10px;
            display: flex;
            flex-direction: column;
        }

        .message {
            margin-bottom: 10px;
            display: flex;
            align-items: center;
        }

        .sent {
            justify-content: flex-end;
        }

        .received {
            justify-content: flex-start;
        }

        .message p {
            margin: 0;
            padding: 5px 10px;
            border-radius: 10px;
        }

        .sent p {
            background-color: #a1f7d1;  
        }

        .received p {
            background-color: #f1f1f1;
        }

        .timestamp {
            font-size: 0.8em;
            color: gray;
            margin-left: 10px;
        }

        .disabled {
            background-color: #f1f1f1;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
    <h1>채팅방 - Room No: ${roomNo}</h1>
    <div id="chat-box">
        <c:forEach var="message" items="${messages}">
            <div class="message ${message.sender == user ? 'sent' : 'received'}">
                <p>${message.msgContent}</p>
                <span class="timestamp">
                    <fmt:formatDate value="${message.msgDate}" pattern="yyyy-MM-dd HH:mm" />
                </span>
            </div>
        </c:forEach>
    </div>
    <input type="text" id="message" placeholder="메시지를 입력하세요" />
    <button onclick="sendMessage()">전송</button>
    <button onclick="leaveChatRoom()" id="leaveButton">나가기</button>

    <script>
        let ws;
        const roomNo = "${roomNo}";
        const chatBox = document.getElementById("chat-box");
        const messageInput = document.getElementById("message");
        const sendButton = document.querySelector("button[onclick='sendMessage()']");
        const leaveButton = document.getElementById("leaveButton");

        function initializeWebSocket() {
            ws = new WebSocket(`ws://localhost:8080/chat/${roomNo}`);
            ws.onmessage = function(event) {
                const messageData = event.data.split(",");
                const messageContent = messageData[0];
                const messageTime = messageData[1];

                const messageDiv = document.createElement("div");
                if (messageContent === "상대방이 채팅방에서 퇴장하였습니다.") {
                    messageDiv.classList.add("message", "received");
                    const messageText = document.createElement("p");
                    messageText.textContent = messageContent;
                    messageDiv.appendChild(messageText);

                    const timestampSpan = document.createElement("span");
                    timestampSpan.classList.add("timestamp");
                    timestampSpan.textContent = messageTime;
                    messageDiv.appendChild(timestampSpan);

                    chatBox.appendChild(messageDiv);
                    disableChatFunctionality();
                    leaveButton.disabled = false;
                    leaveButton.classList.remove("disabled");
                } else {
                    messageDiv.classList.add("message", "received");
                    const messageText = document.createElement("p");
                    messageText.textContent = messageContent;
                    messageDiv.appendChild(messageText);

                    const timestampSpan = document.createElement("span");
                    timestampSpan.classList.add("timestamp");
                    const timestamp = new Date(messageTime);
                    const formattedTime = timestamp.getFullYear() + "-" + 
                                          String(timestamp.getMonth() + 1).padStart(2, '0') + "-" + 
                                          String(timestamp.getDate()).padStart(2, '0') + " " + 
                                          String(timestamp.getHours()).padStart(2, '0') + ":" + 
                                          String(timestamp.getMinutes()).padStart(2, '0');

                    timestampSpan.textContent = formattedTime;
                    messageDiv.appendChild(timestampSpan);
                    chatBox.appendChild(messageDiv);
                }
                chatBox.scrollTop = chatBox.scrollHeight;
            };
        }

        function sendMessage() {
            const message = messageInput.value;
            if (message.trim() !== "") {
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

                messageInput.value = "";
                const messageData = message + "," + formattedTime;
                ws.send(messageData);
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        }

        messageInput.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                sendMessage();
            }
        });

        function leaveChatRoom() {
            const userConfirmed = confirm("정말로 채팅방을 나가시겠습니까? 채팅내용이 모두 삭제됩니다.");
            if (userConfirmed) {
                if (ws) {
                    ws.close();
                    console.log("WebSocket connection closed.");
                }

                disableChatFunctionality();
                chatBox.innerHTML = "";
                alert("채팅방을 나갔습니다.");
            }
        }

        function disableChatFunctionality() {
            messageInput.disabled = true;
            sendButton.disabled = true;
            leaveButton.disabled = true;
            messageInput.classList.add("disabled");
            sendButton.classList.add("disabled");
            leaveButton.classList.add("disabled");

            if (leaveButton.disabled === false) {
                leaveButton.classList.remove("disabled");
            }
        }

        window.onload = initializeWebSocket;
    </script>
</body>
</html>
