document.addEventListener("DOMContentLoaded", function () {
    const roomNo = new URLSearchParams(window.location.search).get("roomNo");
    const token = localStorage.getItem("Token");

    if (!roomNo || !token) return;

    const userNo = getUserNoFromToken(token);
    const displayedMessageIds = new Set();

    const chatBox = document.getElementById("chat-box");
    const inputField = document.getElementById("message-input");
    const sendButton = document.getElementById("send-message");

    let socket = null;
    let reconnectAttempts = 0;
    const maxReconnectAttempts = 5;

    // WebSocket 연결 함수
    function connectWebSocket() {
        socket = new WebSocket(`ws://${window.location.host}/ws/chat/${roomNo}?token=${encodeURIComponent(token)}`);

        socket.onopen = () => {
            console.log("웹소켓 연결 성공");
            reconnectAttempts = 0;
        };

        socket.onmessage = (event) => {
            try {
                const chatMessage = JSON.parse(event.data);

                // 중복 메시지 방지
                if (!chatMessage.msgNo || displayedMessageIds.has(chatMessage.msgNo)) return;

                displayedMessageIds.add(chatMessage.msgNo);
                displayMessage(chatMessage, userNo);
            } catch (e) {
                console.error("❌ 메시지 파싱 실패:", e);
            }
        };

        socket.onerror = (error) => {
            console.error("❌ 웹소켓 오류:", error);
        };

        socket.onclose = () => {
            console.warn("⚠️ 웹소켓 연결 종료됨");
            if (reconnectAttempts < maxReconnectAttempts) {
                reconnectAttempts++;
                setTimeout(connectWebSocket, 3000); // 재시도
            } else {
                alert("서버와의 연결이 반복적으로 실패했습니다. 새로고침 해주세요.");
            }
        };
    }

    connectWebSocket();

    // 메시지 전송 이벤트
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

            // displayMessage 호출 생략: 서버 응답에서 처리
        } else {
            alert("채팅 서버와 연결이 끊어졌습니다.");
        }
    });

    // Enter 키로 메시지 전송
    inputField.addEventListener("keydown", (event) => {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            sendButton.click();
        }
    });

    // JWT 토큰에서 userNo 추출
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

    // 메시지 화면에 표시
    function displayMessage(chatMessage, userNo) {
        const isMine = chatMessage.senderNo === userNo;

        const messageDiv = document.createElement("div");
        messageDiv.classList.add("message", isMine ? "my-message" : "other-message");

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
});
