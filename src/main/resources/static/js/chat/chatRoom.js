import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js'; 

document.addEventListener("DOMContentLoaded", function () {
    const roomNo = new URLSearchParams(window.location.search).get("roomNo");
    const token = localStorage.getItem("Token");

    if (!roomNo || !token) return;

    const userNo = getUserNoFromToken(token);
    const displayedMessageIds = new Set();

    const chatBox = document.getElementById("chat-box");
    const inputField = document.getElementById("message-input");
    const sendButton = document.getElementById("send-message");
    const leaveButton = document.getElementById("leave-chat-room");

    let socket = null;
    let reconnectAttempts = 0;
    const maxReconnectAttempts = 5;
    let isCurrentRoom = false;

    function connectWebSocket() {
        socket = new WebSocket(`ws://${window.location.host}/ws/chat?roomNo=${roomNo}&userNo=${userNo}&token=${token}`);

        socket.onopen = () => {
            console.log("웹소켓 연결 성공");
            reconnectAttempts = 0;
            isCurrentRoom = true;
        };

        socket.onmessage = async (event) => {
            try {
                const chatMessage = JSON.parse(event.data);

                if (chatMessage.messageType === "READ_UPDATE") {
                    updateMessageDisplay(chatMessage.msgNo, true);

                } else if (chatMessage.messageType === "LEAVE") {
                    if (chatMessage.userNo !== userNo) {
                        displayLeaveMessage(chatMessage.content);
                    }

                } else {
                    if (!chatMessage.msgNo || displayedMessageIds.has(chatMessage.msgNo)) return;

                    displayedMessageIds.add(chatMessage.msgNo);
                    displayMessage(chatMessage, userNo);

                    // 내 메시지가 아닐 경우 자동 읽음 처리
                    if (
                        chatMessage.senderNo !== userNo &&
                        isCurrentRoom &&
                        document.visibilityState === 'visible'
                    ) {
                        await fetchWithAuth(`/api/chat/messages/read/${chatMessage.msgNo}`, {
                            method: "POST"
                        });

                        // 아이콘도 바로 반영
                        updateMessageDisplay(chatMessage.msgNo, true);
                    }
                }
            } catch (e) {
                console.error("메시지 처리 오류:", e);
            }
        };

        socket.onerror = (error) => {
            console.error("웹소켓 오류:", error);
        };

        socket.onclose = () => {
            console.warn("웹소켓 연결 종료됨");
            isCurrentRoom = false;
            if (reconnectAttempts < maxReconnectAttempts) {
                reconnectAttempts++;
                setTimeout(connectWebSocket, 3000);
            } else {
                alert("서버와의 연결이 반복적으로 실패했습니다. 새로고침 해주세요.");
            }
        };
    }

    connectWebSocket();

    sendButton.addEventListener("click", () => {
        const content = inputField.value.trim();
        if (!content) return;

        const message = {
            roomNo: roomNo,
            msgContent: content,
            senderNo: userNo,
        };

        if (socket.readyState === WebSocket.OPEN) {
            socket.send(JSON.stringify(message));
            inputField.value = "";
        } else {
            alert("채팅 서버와 연결이 끊어졌습니다.");
        }
    });

    inputField.addEventListener("keydown", (event) => {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            sendButton.click();
        }
    });

    function getUserNoFromToken(token) {
        try {
            const base64 = token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/");
            const decoded = atob(base64);
            const jsonPayload = decodeURIComponent(
                Array.from(decoded).map(c =>
                    '%' + c.charCodeAt(0).toString(16).padStart(2, '0')
                ).join('')
            );
            return JSON.parse(jsonPayload)?.userNo || null;
        } catch {
            return null;
        }
    }

    function displayMessage(chatMessage, userNo) {
        const isMine = chatMessage.senderNo === userNo;

        const messageDiv = document.createElement("div");
        messageDiv.classList.add("message", isMine ? "my-message" : "other-message");
        messageDiv.dataset.msgNo = chatMessage.msgNo;

        if (isMine) {
            const readStatusIcon = document.createElement("span");
            readStatusIcon.classList.add("read-status");
            readStatusIcon.innerHTML = chatMessage.msgIsRead
                ? '<i class="fas fa-check-circle" style="color: green;"></i>'
                : '<i class="fas fa-question-circle" style="color: red;"></i>';
            messageDiv.appendChild(readStatusIcon);
        }

        const messageContent = document.createElement("p");
        messageContent.textContent = chatMessage.msgContent;

        const timestamp = document.createElement("span");
        timestamp.classList.add("timestamp");
        timestamp.textContent = chatMessage.formattedMsgDate || "";

        messageDiv.appendChild(messageContent);
        messageDiv.appendChild(timestamp);

        chatBox.appendChild(messageDiv);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    function updateMessageDisplay(msgNo, isRead) {
        const msgSelector = `.message[data-msg-no="${msgNo}"]`;
        const msgElement = document.querySelector(msgSelector);
        if (!msgElement) return;

        const readStatusIcon = msgElement.querySelector('.read-status');
        if (readStatusIcon) {
            readStatusIcon.innerHTML = isRead 
                ? '<i class="fas fa-check-circle" style="color: green;"></i>' 
                : '<i class="fas fa-question-circle" style="color: red;"></i>';
        }
    }

    function displayLeaveMessage(content) {
        const leaveMessageDiv = document.createElement("div");
        leaveMessageDiv.textContent = content;
        leaveMessageDiv.classList.add("leave-message");
        chatBox.appendChild(leaveMessageDiv);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    leaveButton.addEventListener("click", () => {
        if (socket.readyState === WebSocket.OPEN) {
            socket.send(JSON.stringify({ messageType: "LEAVE" }));
            socket.close();
        }
    });
});
