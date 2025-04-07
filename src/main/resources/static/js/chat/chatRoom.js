document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("Token");

    const isItemDetailPage = document.getElementById("inquiryBtn") || document.querySelector(".inquiry-button");

    if (!isItemDetailPage) {
        console.log("📌 채팅방 접속");
        return;
    }

    let inquiryBtn = document.getElementById("inquiryBtn") || document.querySelector(".inquiry-button");

    console.log("✅ 문의하기 버튼을 찾았습니다.");

    if (!token) {
        alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
        window.location.href = "/login";
        return;
    }

    inquiryBtn.addEventListener("click", async function () {
        console.log("📌 문의하기 버튼 클릭");

        inquiryBtn.textContent = "문의 중...";
        inquiryBtn.disabled = true;

        const pathname = window.location.pathname;
        const itemNo = pathname.split("/").pop();
        const userNo = getUserNoFromToken(token);

        if (!userNo) {
            console.error("❌ 로그인한 사용자 정보 없음.");
            resetButton();
            return;
        }

        if (!itemNo || isNaN(parseInt(itemNo, 10))) {
            console.error("❌ 아이템 정보 없음");
            resetButton();
            return;
        }

        try {
            const itemData = await fetchItemData(itemNo, token);
            if (!itemData) {
                resetButton();
                return;
            }

            const { sellerNo, itemTitle } = itemData;

            console.log(`✅ 가져온 sellerNo: ${sellerNo}, itemTitle: ${itemTitle}`);

            const roomNo = await fetchExistingChatRoom(itemNo, sellerNo, userNo, token);

            if (roomNo) {
                console.log("✅ 기존 채팅방으로 이동:", roomNo);
                location.href = `/chat/chatRoom?roomNo=${roomNo}`;
            } else {
                console.log("❌ 기존 채팅방 없음, 새로 생성");
                await createChatRoom(sellerNo, userNo, itemNo, itemTitle, token);
            }
        } catch (error) {
            console.error("🚨 오류:", error.message || error);
        } finally {
            resetButton();
        }
    });

    function resetButton() {
        inquiryBtn.textContent = "문의하기";
        inquiryBtn.disabled = false;
    }

    function getUserNoFromToken(token) {
        try {
            const base64Url = token.split(".")[1];
            const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
            const jsonPayload = decodeURIComponent(escape(window.atob(base64)));
            return JSON.parse(jsonPayload)?.userNo || null;
        } catch (error) {
            console.error("❌ JWT 파싱 실패:", error);
            return null;
        }
    }

    async function fetchItemData(itemNo, token) {
        try {
            const response = await fetch(`/item/${itemNo}`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                throw new Error(`🚨 아이템 정보를 가져오지 못함 (상태 코드: ${response.status})`);
            }

            return await response.json();
        } catch (error) {
            console.error(error.message || error);
            return null;
        }
    }

    async function fetchExistingChatRoom(itemNo, sellerNo, userNo, token) {
        try {
            console.log(`🚀 채팅방 확인 요청: itemNo=${itemNo}, sellerNo=${sellerNo}, buyerNo=${userNo}`);

            const response = await fetch("/api/chat/room", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    sellerUserNo: sellerNo,
                    buyerUserNo: userNo,
                    itemNo: itemNo,
                }),
            });

            const data = await response.json();
            console.log("🚀 서버 응답:", data);

            return data.roomNo || null;
        } catch (error) {
            console.error("❌ fetchExistingChatRoom 오류:", error);
            return null;
        }
    }

    async function createChatRoom(sellerNo, buyerNo, itemNo, itemTitle, token) {
        try {
            console.log("🚀 채팅방 생성 요청:", { sellerNo, buyerNo, itemNo, itemTitle });

            const response = await fetch("/api/chat/rooms", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ sellerNo, buyerNo, itemNo, itemTitle }),
            });

            const data = await response.json();
            if (response.ok) {
                console.log("✅ 채팅방 생성 성공:", data);
                location.href = `/chat/chatRoom?roomNo=${data.roomNo}`;
                connectWebSocket(data.roomNo);  // 채팅방 생성 후 웹소켓 연결
            } else {
                console.error("❌ 채팅방 생성 실패:", data);
            }
        } catch (error) {
            console.error("🚨 오류 발생:", error);
        }
    }

    function connectWebSocket(roomNo) {
        const socket = new SockJS(`${window.location.origin}/ws`);
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log("웹소켓 연결 성공:", frame);
            
            stompClient.subscribe(`/topic/room/${roomNo}`, function (message) {
                console.log("수신된 메시지:", message.body);
                const chatMessage = JSON.parse(message.body);
                displayMessage(chatMessage);
            });

            // 실시간 채팅 메시지 보내기
            const sendMessage = (messageContent) => {
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
                    roomNo: roomNo,
                    message: messageContent,
                    senderNo: getUserNoFromToken(localStorage.getItem("Token")),
                }));
            };

            // 메시지 전송 버튼 처리
            document.getElementById("send-message").addEventListener("click", function() {
                const messageContent = document.getElementById("message-input").value;
                if (messageContent) {
                    sendMessage(messageContent);
                    document.getElementById("message-input").value = '';  // 입력 후 비우기
                }
            });
        }, function (error) {
            console.error("웹소켓 연결 실패:", error);
        });
    }

    function displayMessage(chatMessage) {
        const chatBox = document.getElementById("chat-box");
        const newMessage = document.createElement("div");
        newMessage.classList.add("message");
        newMessage.classList.add(chatMessage.senderNo === getUserNoFromToken(localStorage.getItem("Token")) ? "my-message" : "other-message");
        newMessage.innerHTML = `<p>${chatMessage.msgContent}</p><span class="timestamp">${chatMessage.timestamp}</span>`;
        chatBox.appendChild(newMessage);
        chatBox.scrollTop = chatBox.scrollHeight;  // 스크롤 바닥으로
    }
});
