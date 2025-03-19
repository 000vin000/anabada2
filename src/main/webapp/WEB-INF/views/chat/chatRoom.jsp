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

        /* 비활성화된 요소 스타일 */
        .disabled {
            background-color: #f1f1f1;
            cursor: not-allowed;
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
    <button onclick="leaveChatRoom()" id="leaveButton">나가기</button> <!-- 나가기 버튼 추가 -->

    <script>
        let ws;
        const roomNo = "${roomNo}";
        const chatBox = document.getElementById("chat-box");
        const messageInput = document.getElementById("message");
        const sendButton = document.querySelector("button[onclick='sendMessage()']");
        const leaveButton = document.getElementById("leaveButton");

        // WebSocket 초기화
        function initializeWebSocket() {
            ws = new WebSocket(`ws://localhost:8080/chat/${roomNo}`);

            // WebSocket 메시지를 받으면 채팅박스에 표시
            ws.onmessage = function(event) {
                const messageData = event.data.split(","); // 시간, 메시지 구분
                const messageContent = messageData[0];
                const messageTime = messageData[1];

                const messageDiv = document.createElement("div");

                if (messageContent === "상대방이 채팅방에서 퇴장하였습니다.") {
                    // 상대방이 퇴장한 메시지 처리
                    messageDiv.classList.add("message", "received");
                    const messageText = document.createElement("p");
                    messageText.textContent = messageContent;
                    messageDiv.appendChild(messageText);

                    const timestampSpan = document.createElement("span");
                    timestampSpan.classList.add("timestamp");
                    timestampSpan.textContent = messageTime; // 시간 표시
                    messageDiv.appendChild(timestampSpan);

                    chatBox.appendChild(messageDiv);

                    // 채팅 기능 비활성화
                    disableChatFunctionality();

                    // 나가기 버튼 활성화
                    leaveButton.disabled = false;
                    leaveButton.classList.remove("disabled"); // 나가기 버튼의 disabled 클래스 제거
                } else {
                    // 일반 메시지 처리
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

                    timestampSpan.textContent = formattedTime; // 시간 표시
                    messageDiv.appendChild(timestampSpan);

                    chatBox.appendChild(messageDiv);
                }

                // 스크롤을 자동으로 맨 아래로 내리기
                chatBox.scrollTop = chatBox.scrollHeight;
            };
        }

        // 메시지 보내기
        function sendMessage() {
            const message = messageInput.value;
            if (message.trim() !== "") {
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
                messageInput.value = "";

                // 서버로 메시지 전송
                const messageData = message + "," + formattedTime;
                ws.send(messageData); // 메시지, 시간

                // 스크롤 자동으로 맨 아래로 내리기
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        }

        // 엔터 키로 메시지 전송
        messageInput.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                sendMessage();
            }
        });

        // 나가기 버튼 클릭 시
        function leaveChatRoom() {
            // 확인 창 표시
            const userConfirmed = confirm("정말로 채팅방을 나가시겠습니까? 채팅내용이 모두 삭제됩니다.");
            if (userConfirmed) {
                // WebSocket 연결 종료
                if (ws) {
                    ws.close();
                    console.log("WebSocket connection closed.");
                }

                // 채팅방 비활성화
                disableChatFunctionality();

                // 채팅 내용 삭제
                chatBox.innerHTML = "";

                // 상품 상세페이지로 리다이렉트 필요
                alert("채팅방을 나갔습니다.");
            }
        }

     	// 채팅 기능 비활성화
        function disableChatFunctionality() {
            messageInput.disabled = true;
            sendButton.disabled = true;
            leaveButton.disabled = true;

            // 비활성화된 상태로 스타일 적용
            messageInput.classList.add("disabled");
            sendButton.classList.add("disabled");
            leaveButton.classList.add("disabled");

            // 나가기 버튼 활성화되면 disabled 클래스 제거
            if (leaveButton.disabled === false) {
                leaveButton.classList.remove("disabled");
            }
        }

        // 페이지가 로드될 때 WebSocket 초기화
        window.onload = initializeWebSocket;
    </script>
</body>
</html>
