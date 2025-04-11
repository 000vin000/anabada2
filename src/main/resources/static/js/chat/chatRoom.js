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
    let isCurrentRoom = false; // 현재 채팅방 여부를 나타내는 변수

    // WebSocket 연결 함수
    function connectWebSocket() {
        socket = new WebSocket(`ws://${window.location.host}/ws/chat?roomNo=${roomNo}&userNo=${userNo}&token=${token}`);

        socket.onopen = () => {
            console.log("웹소켓 연결 성공");
            reconnectAttempts = 0;
            isCurrentRoom = true; // 연결 성공 시 현재 채팅방으로 설정
        };

        socket.onmessage = async (event) => {
            try {
                const chatMessage = JSON.parse(event.data);

                if (chatMessage.messageType === "READ_UPDATE") {
                    updateMessageDisplay(chatMessage.msgNo, true); // 메시지 읽음 처리
                } else if (chatMessage.messageType === "LEAVE") {
                    if (chatMessage.userNo !== userNo) {
                        displayLeaveMessage(chatMessage.content); // 퇴장 메시지 표시
                    }
                } else {
                    if (!chatMessage.msgNo || displayedMessageIds.has(chatMessage.msgNo)) return;

                    displayedMessageIds.add(chatMessage.msgNo);
                    displayMessage(chatMessage, userNo);

                    // 자동 읽음 처리
                    if (isCurrentRoom && document.visibilityState === 'visible') {
                        const response = await fetchWithAuth(`/api/chat/messages/read/${chatMessage.msgNo}`, {
                            method: "POST"
                        });

                        if (!response.ok) {
                            console.error("메시지 읽음 처리 실패:", response.status);
                        }
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
            isCurrentRoom = false; // 연결 종료 시 현재 채팅방 상태 변경
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
	        inputField.value = ""; // 입력 필드 초기화

	        // 메시지 읽음 상태를 즉시 업데이트
	        updateMessageDisplay(message.msgNo, true); 
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
	    messageDiv.dataset.msgNo = chatMessage.msgNo; // msgNo 데이터 속성 추가

	    // 자신의 메시지일 때만 읽음 상태 아이콘 추가
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
	    chatBox.scrollTop = chatBox.scrollHeight; // 스크롤을 최신 메시지로 이동
	}

    // 메시지 읽음 상태 업데이트 함수
	function updateMessageDisplay(msgNo, isRead) {
	    const messages = document.querySelectorAll('.message');
	    messages.forEach(msg => {
	        if (msg.dataset.msgNo === msgNo) {
	            const readStatusIcon = msg.querySelector('.read-status');
	            if (readStatusIcon) {
	                readStatusIcon.innerHTML = isRead 
	                    ? '<i class="fas fa-check-circle" style="color: green;"></i>' 
	                    : '<i class="fas fa-question-circle" style="color: red;"></i>';
	            } else {
	                console.warn(`읽음 상태 아이콘을 찾을 수 없음: msgNo=${msgNo}`);
	            }
	        }
	    });
	}




    // 퇴장 메시지 표시 함수
    function displayLeaveMessage(content) {
        const leaveMessageDiv = document.createElement("div");
        leaveMessageDiv.textContent = content;
        leaveMessageDiv.classList.add("leave-message");
        chatBox.appendChild(leaveMessageDiv);
        chatBox.scrollTop = chatBox.scrollHeight; // 스크롤을 최신 메시지로 이동
    }

    // 나가기 버튼 클릭 이벤트 처리
    leaveButton.addEventListener("click", () => {
        if (socket.readyState === WebSocket.OPEN) {
            socket.send(JSON.stringify({ messageType: "LEAVE" }));
            socket.close(); // WebSocket 연결 종료
        }
    });
});
