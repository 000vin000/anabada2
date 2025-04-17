import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js'; 

export function initChatRoomList() {
	const btn = document.querySelector(".viewChatRoomsBtn");
	if (!btn) return;

	const modal = document.getElementById("chatRoomModal");
	const listBox = document.getElementById("chat-room-list-box");
	const closeBtn = document.getElementById("closeChatModal");

	// 닫기 이벤트 등록
	if (closeBtn) {
		closeBtn.addEventListener("click", () => {
			modal.style.display = "none";
		});
	}

	btn.addEventListener("click", async function () {
		const itemNo = this.dataset.itemNo;

		try {
			const res = await fetchWithAuth(`/api/chat/rooms/item/${itemNo}`, {
				method: "GET",
				credentials: "include"
			});

			if (!res.ok) {
				const errorText = await res.text();
				throw new Error(`서버 오류: ${errorText}`);
			}

			const data = await res.json();

			if (!listBox || !modal) {
				console.warn("chatRoomModal 또는 listBox가 존재하지 않습니다.");
				return;
			}

			listBox.innerHTML = ""; // 초기화
			modal.style.display = "block"; // 모달 표시

			if (data.length === 0) {
				listBox.innerHTML = "<p>현재 채팅 중인 사람이 없습니다.</p>";
				return;
			}

			data.forEach(room => {
			  const div = document.createElement("div");
			  div.className = "chat-room-item";

			  const date = room.lastMessageTime ? new Date(room.lastMessageTime) : null;
			  const formattedTime = date ? date.toLocaleString('ko-KR', {
			    year: 'numeric',
			    month: '2-digit',
			    day: '2-digit',
			    hour: '2-digit',
			    minute: '2-digit'
			  }) : '';

			  div.innerHTML = `
			    <div class="chat-room-info">
			      <a href="/chat/chatRoom?roomNo=${room.roomNo}" class="chat-room-link">
			        <span class="chat-room-nickname">[${room.buyerNickname}]님과의 채팅방</span>
			        ${room.unreadCount > 0 ? `<span class="unread-count">${room.unreadCount}</span>` : ''}
			      </a>
			      <p class="last-message">
			        ${room.lastMessage || '최근 메시지가 없습니다.'}
			      </p>
			      <small class="last-message-time">${formattedTime}</small>
			    </div>
			  `;

			  listBox.appendChild(div);
			});





		} catch (err) {
			alert("채팅방 목록 불러오기 실패");
			console.error("오류:", err);
		}
	});
}
