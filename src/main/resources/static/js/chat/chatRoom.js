document.addEventListener("DOMContentLoaded", async function () {
    const inquiryBtn = document.getElementById("inquiryBtn");
    const token = localStorage.getItem("Token");

    if (!inquiryBtn) {
        console.error("❌ 문의하기 버튼을 찾을 수 없습니다.");
        return;
    }
    console.log("✅ 문의하기 버튼을 찾았습니다.");

    if (!token) {
        alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
        window.location.href = "/login";
        return;
    }

    inquiryBtn.addEventListener("click", async function () {
        console.log("문의하기 버튼 클릭");

        const originalText = inquiryBtn.textContent;
        inquiryBtn.textContent = "문의 중...";
        inquiryBtn.disabled = true;

        const pathname = window.location.pathname;
        const itemNo = pathname.split("/").pop();
        const userNo = getUserNoFromToken(token);

        if (!userNo) {
            console.error("로그인한 사용자 정보 없음.");
            resetButton();
            return;
        }
        if (!itemNo || isNaN(parseInt(itemNo, 10))) {
            console.error("아이템 정보 없음");
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
            const existingRoom = await fetchExistingChatRoom(itemNo, sellerNo, userNo, token);

            if (existingRoom) {
                console.log("기존 채팅방:", existingRoom);
                location.href = `/api/chat/${existingRoom}`;
            } else {
                console.log("채팅방이 없으므로 새로 생성");
                await createChatRoom(sellerNo, itemNo, itemTitle, token);
            }
        } catch (error) {
            console.error("오류:", error.message || error);
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
            console.error("JWT 파싱 실패:", error);
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
                throw new Error(`아이템 정보를 가져오지 못함 (상태 코드: ${response.status})`);
            }
            return await response.json();
        } catch (error) {
            console.error(error.message || error);
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
                body: JSON.stringify({
                    sellerUserNo: parseInt(sellerNo, 10),
                    buyerUserNo: parseInt(userNo, 10),
                    itemNo: parseInt(itemNo, 10),
                }),
            });

            if (!response.ok) {
                throw new Error(`기존 채팅방 확인 요청 실패: ${response.status}`);
            }
            return (await response.json()).roomNo || null;
        } catch (error) {
            console.error(error.message || error);
            return null;
        }
    }

    async function createChatRoom(sellerNo, itemNo, itemTitle, token) {
        try {
            if (!sellerNo || !itemNo || !itemTitle) {
                throw new Error("채팅방 생성에 필요한 정보 부족");
            }

            console.log("채팅방 생성 요청:", { sellerNo, itemNo, itemTitle });

            const response = await fetch("/api/chat/rooms", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ sellerNo, itemNo, itemTitle })
            });

            if (!response.ok) {
                throw new Error("채팅방 생성 실패: " + await response.text());
            }
            const data = await response.json();
            location.href = `/api/chat/${data.roomNo}`;
        } catch (error) {
            console.error(error.message || error);
        }
    }
});
