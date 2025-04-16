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
				div.innerHTML = `
					<a href="/chat/chatRoom?roomNo=${room.roomNo}">
						[${room.buyerNickname}]님과의 채팅방
					</a>
				`;
				listBox.appendChild(div);
			});
		} catch (err) {
			alert("채팅방 목록 불러오기 실패");
			console.error("오류:", err);
		}
	});
}
