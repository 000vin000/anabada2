export function initChatRoomList() {
	const btn = document.querySelector(".viewChatRoomsBtn");
	if (!btn) return;

	btn.addEventListener("click", function () {
		const itemNo = this.dataset.itemNo;

		fetch(`/api/chat/rooms/item/${itemNo}`, {
			method: "GET",
			headers: {
				"Authorization": "Bearer " + localStorage.getItem("jwtToken")
			}
		})
		.then(res => res.json())
		.then(data => {
			const listBox = document.getElementById("chat-room-list-box");
			listBox.innerHTML = "";

			if (data.length === 0) {
				listBox.innerHTML = "<p>현재 채팅 중인 사람이 없습니다.</p>";
				return;
			}

			data.forEach(room => {
				const div = document.createElement("div");
				div.innerHTML = `
					<a href="/chat/room/${room.roomNo}">
						[${room.buyerNickname}]님과의 채팅방
					</a>
				`;
				listBox.appendChild(div);
			});
		})
		.catch(err => {
			alert("채팅방 목록 불러오기 실패");
			console.error(err);
		});
	});
}
