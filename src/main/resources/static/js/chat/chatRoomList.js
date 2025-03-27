document.addEventListener("DOMContentLoaded", function () {
    const chatRoomListBtn = document.getElementById("chatList"); // '문의하기' 버튼
    const createRoomBtn = document.getElementById("createRoomBtn"); // 채팅방 생성 버튼
    const chatListContainer = document.getElementById("chat-room-list-container"); // 채팅방 목록 컨테이너
    const sellerId = localStorage.getItem("sellerId");  // 판매자 정보 가져오기
    const token = localStorage.getItem("Token");  // 토큰 가져오기

    // 현재 페이지의 URL 경로에서 itemNo 추출
    const pathname = window.location.pathname;
    const itemNo = pathname.split('/').pop();  // URL 경로에서 마지막 부분을 itemNo로 추출 (예: "/item/detail/3"에서 "3"을 추출)

    // 디버깅을 위한 로그
    console.log("현재 URL 경로:", pathname);
    console.log("추출된 itemNo:", itemNo);

    if (!itemNo) {
        console.error("itemNo가 URL에서 찾을 수 없습니다.");
        return;
    }

    const itemTitle = document.querySelector(".item-name") ? document.querySelector(".item-name").textContent : '';  // HTML에서 아이템 제목 추출

    // 디버깅을 위한 로그
    console.log("아이템 제목:", itemTitle);

    if (!itemTitle) {
        console.error("itemTitle이 페이지에서 찾을 수 없습니다.");
        return;
    }

    if (!chatRoomListBtn) { 
        console.error("문의하기 버튼을 찾을 수 없습니다."); 
        return; 
    } 

    if (!token) { 
        console.warn("로그인이 필요합니다."); 
        alert("로그인이 필요합니다. 로그인 페이지로 이동합니다."); 
        window.location.href = "/auth/login.html"; 
        return; 
    }

    // 채팅방 목록을 UI에 표시하는 함수
    function updateChatRoomListUI(chatRooms) { 
        if (!chatListContainer) { 
            console.error("채팅방 목록 컨테이너를 찾을 수 없습니다."); 
            return; // 더 이상 진행하지 않음 
        } 

        chatListContainer.innerHTML = ''; // 기존 목록 초기화 

        if (!chatRooms || chatRooms.length === 0) { 
            chatListContainer.innerHTML = "<p>참여 중인 채팅방이 없습니다.</p>"; 
            createRoomBtn.style.display = 'block';  // 채팅방이 없으면 생성 버튼 표시 
        } else { 
            let chatListHtml = "<ul>"; 
            chatRooms.forEach(chatRoom => { 
                chatListHtml += `<li><a href="/chat/room/${chatRoom.roomNo}">채팅방: ${chatRoom.itemTitle}</a></li>`; 
            }); 
            chatListHtml += "</ul>"; 
            chatListContainer.innerHTML = chatListHtml; 
            createRoomBtn.style.display = 'none';  // 채팅방이 있으면 생성 버튼 숨김 
        } 
    } 

    // fetchWithAuth 함수 (토큰을 포함한 인증 요청)
    async function fetchWithAuth(url, options = {}) { 
        const headers = { 
            ...options.headers, 
            Authorization: `Bearer ${token}`,  // 토큰을 헤더에 삽입 
            "Content-Type": "application/json", 
        }; 

        const response = await fetch(url, { ...options, headers }); 
        return response; 
    }

    // 채팅방 목록 가져오기 (JWT 자동 포함)
    async function fetchChatRooms() { 
        if (!itemNo || !itemTitle) { 
            alert("아이템 정보가 없습니다."); 
            return; 
        }

        try { 
            const response = await fetchWithAuth(`/api/chat/rooms/check/${itemNo}`, { 
                method: "GET", 
                credentials: "include", // JWT 자동 포함 
            }); 

            if (!response.ok) { 
                if (response.status === 401) { 
                    alert("로그인이 필요합니다. 로그인 페이지로 이동합니다."); 
                    window.location.href = "/auth/login.html"; 
                } else {
                    throw new Error("채팅방 목록을 불러오는 데 실패했습니다."); 
                }
            }

            const data = await response.json(); 
            updateChatRoomListUI(data); 
        } catch (error) { 
            console.error("채팅방 목록 불러오기 실패:", error); 
            alert("채팅방 목록을 불러오는 데 실패했습니다."); 
        } 
    }

    // 채팅방 생성 (JWT 자동 포함)
    async function createChatRoom() { 
        if (!sellerId || !itemNo || !itemTitle) { 
            alert("판매자 정보 또는 아이템 정보가 없습니다."); 
            return; 
        }

        try { 
            const response = await fetchWithAuth(`/api/chat/rooms?sellerId=${sellerId}&itemNo=${itemNo}&itemTitle=${itemTitle}`, { 
                method: "POST", 
                credentials: "include", // JWT 자동 포함 
            });

            if (!response.ok) { 
                if (response.status === 401) { 
                    alert("로그인이 필요합니다. 로그인 페이지로 이동합니다."); 
                    window.location.href = "/auth/login.html"; 
                } else {
                    throw new Error("채팅방 생성에 실패했습니다.");
                }
            }

            alert("채팅방이 생성되었습니다."); 
            fetchChatRooms(); // 새로 생성된 채팅방을 반영하여 목록 갱신 
        } catch (error) { 
            console.error("채팅방 생성 실패:", error); 
            alert("채팅방 생성에 실패했습니다."); 
        } 
    }

    // '문의하기' 버튼 클릭 시 채팅방 목록 보여주기
    chatRoomListBtn.addEventListener("click", function() { 
        fetchChatRooms(); 
        openWindow('QnaWindow', '/api/chat/rooms'); 
    });

    // 채팅방 생성 버튼 클릭 시 새 채팅방 생성
    createRoomBtn.addEventListener("click", function() { 
        createChatRoom(); 
    });
});
