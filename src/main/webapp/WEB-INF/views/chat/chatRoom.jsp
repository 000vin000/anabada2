<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

        /* 입력란 및 버튼 스타일 */
        input[type="text"] {
            width: 80%;
            padding: 10px;
            margin-top: 10px;
        }

        button {
            padding: 10px;
            width: 15%;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <h1>채팅방 - Room No: ${roomNo}</h1>
    <div id="chat-box"></div>
    <input type="text" id="message" placeholder="메시지를 입력하세요" />
    <button onclick="sendMessage()">전송</button>

    <script>
        const roomNo = "${roomNo}";
        const ws = new WebSocket(`ws://localhost:8080/chat/${roomNo}`);

        // WebSocket 메시지를 받으면 채팅박스에 표시
        ws.onmessage = function(event) {
            const chatBox = document.getElementById("chat-box");
            
            // 받은 메시지 표시
            const messageDiv = document.createElement("div");
            messageDiv.classList.add("message", "received");
            const messageText = document.createElement("p");
            messageText.textContent = event.data;
            messageDiv.appendChild(messageText);
            chatBox.appendChild(messageDiv);

            // 스크롤을 자동으로 맨 아래로 내리기
            chatBox.scrollTop = chatBox.scrollHeight;
        };

        // 메시지 보내기 함수
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
                chatBox.appendChild(messageDiv);

                // 입력란 비우기
                document.getElementById("message").value = "";

                // 서버로 메시지 전송
                ws.send(message);
                // 스크롤을 자동으로 맨 아래로 내리기
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
