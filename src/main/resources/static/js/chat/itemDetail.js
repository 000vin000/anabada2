export function initInquiryButton() {
	const inquiryBtn = document.getElementById("inquiryBtn") || document.querySelector(".inquiryBtn");
	if (!inquiryBtn) return;

	inquiryBtn.addEventListener("click", async function () {
		const token = localStorage.getItem("Token");

		if (!token) {
			Swal.fire({
				title: '로그인이 필요합니다',
				text: '로그인 후 사용하실 수 있습니다.',
				icon: 'warning',
				showCancelButton: true,
				confirmButtonText: '로그인',
				cancelButtonText: '취소',
				reverseButtons: true,
				customClass: {
					confirmButton: 'swal2-confirm-btn',
					cancelButton: 'swal2-cancel-btn'
				}
			}).then((result) => {
				if (result.isConfirmed) {
					window.location.href = "/login";
				} else {
					resetButton(); // 취소를 누르면 버튼 상태 초기화
				}
			});
			return; // 로그인하지 않은 경우 종료
		}

		inquiryBtn.textContent = "문의 중...";
		inquiryBtn.disabled = true;

		const itemNo = window.location.pathname.split("/").pop();
		const userNo = getUserNoFromToken(token);

		if (!userNo || !itemNo || isNaN(parseInt(itemNo))) {
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
			const roomNo = await fetchExistingChatRoom(itemNo, sellerNo, userNo, token);

			if (roomNo) {
				location.href = `/chat/chatRoom?roomNo=${roomNo}`;
			} else {
				await createChatRoom(sellerNo, userNo, itemNo, itemTitle, token);
			}
		} catch (error) {
			console.error("오류:", error);
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
			const base64 = token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/");
			const jsonPayload = decodeURIComponent(escape(window.atob(base64)));
			return JSON.parse(jsonPayload)?.userNo || null;
		} catch {
			return null;
		}
	}

	async function fetchItemData(itemNo, token) {
		try {
			const response = await fetch(`/item/${itemNo}`, {
				headers: { "Authorization": `Bearer ${token}` }
			});
			if (!response.ok) throw new Error("아이템 정보 가져오기 실패");
			return await response.json();
		} catch (e) {
			console.error(e);
			return null;
		}
	}

	async function fetchExistingChatRoom(itemNo, sellerNo, userNo, token) {
		try {
			const response = await fetch("/api/chat/room", {
				method: "POST",
				headers: {
					"Authorization": `Bearer ${token}`,
					"Content-Type": "application/json",
				},
				body: JSON.stringify({ sellerUserNo: sellerNo, buyerUserNo: userNo, itemNo })
			});
			const data = await response.json();
			return data.roomNo || null;
		} catch (e) {
			console.error("fetchExistingChatRoom 오류:", e);
			return null;
		}
	}

	async function createChatRoom(sellerNo, buyerNo, itemNo, itemTitle, token) {
		try {
			const response = await fetch("/api/chat/rooms", {
				method: "POST",
				headers: {
					"Authorization": `Bearer ${token}`,
					"Content-Type": "application/json",
				},
				body: JSON.stringify({ sellerNo, buyerNo, itemNo, itemTitle })
			});

			const data = await response.json();
			if (response.ok) {
				location.href = `/chat/chatRoom?roomNo=${data.roomNo}`;
			} else {
				console.error("채팅방 생성 실패:", data);
			}
		} catch (e) {
			console.error("채팅방 생성 오류:", e);
		}
	}
}
