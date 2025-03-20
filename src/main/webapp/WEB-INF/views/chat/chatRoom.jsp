<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
        .message { margin-bottom: 10px; }
        .sent { text-align: right; }
        .received { text-align: left; }
    </style>
</head>
<body>
    <h1>채팅방 - Room No: ${roomNo}</h1>
    <div id="chat-box"></div>
    <input type="text" id="message" placeholder="메시지를 입력하세요" />
    <button onclick="sendMessage()">전송</button>

    <script>
        let ws;
        const roomNo = "${roomNo}";
        const chatBox = document.getElementById("chat-box");
        const messageInput = document.getElementById("message");

        window.onload = function () {
            loadPreviousMessages();
            initializeWebSocket();
        };

        function loadPreviousMessages() {
            fetch(`/chat/messages/${roomNo}`)
                .then(response => response.json())
                .then(messages => {
                    messages.forEach(msg => {
                        const messageDiv = document.createElement("div");
                        messageDiv.className = "message received";
                        messageDiv.innerHTML = `<p>${msg.msgContent}</p><span>${msg.msgDate}</span>`;
                        chatBox.appendChild(messageDiv);
                    });
                    chatBox.scrollTop = chatBox.scrollHeight;
                })
                .catch(error => console.error("메시지 로딩 실패:", error));
        }

        function initializeWebSocket() {
            ws = new WebSocket(`ws://localhost:8080/chat/${roomNo}`);
            ws.onmessage = function (event) {
                const messageDiv = document.createElement("div");
                messageDiv.className = "message received";
                messageDiv.innerHTML = `<p>${event.data}</p>`;
                chatBox.appendChild(messageDiv);
                chatBox.scrollTop = chatBox.scrollHeight;
            };
        }

        function sendMessage() {
            const message = messageInput.value;
            if (message.trim() !== "") {
                const messageDiv = document.createElement("div");
                messageDiv.className = "message sent";
                messageDiv.innerHTML = `<p>${message}</p>`;
                chatBox.appendChild(messageDiv);

                ws.send(message);
                messageInput.value = "";
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        }
    </script>
</body>
</html>