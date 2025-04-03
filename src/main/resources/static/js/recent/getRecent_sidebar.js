// 최근 본 목록 가져오기
function getRecentItems() {
    return JSON.parse(localStorage.getItem(RECENT_ITEMS_KEY)) || {};
}

// 최근 본 상품 목록을 화면에 표시하는 함수
function displayRecentViews() {
	const container = document.getElementById("recently-viewed");
	if (!container) {
		return;
	}

    let recentItems = Object.values(getRecentItems()).flat();
	const item = recentItems[0]; // 첫 번째 아이템만 가져오기

    if (recentItems.length > 0) {
	    container.innerHTML = ""; // 기존 목록 초기화
        // 배열이 비어있지 않은 경우 실행
        const itemElement = document.createElement("div");
        itemElement.classList.add("recent_item");

        itemElement.innerHTML = `
            <a href="/recent">
                <img src="${item.image_url}" alt="recent" width="60" height="60">
            </a>
        `;

        container.appendChild(itemElement);
    }
}

// 페이지 로드 시 최근 본 상품 목록 표시
document.addEventListener("DOMContentLoaded", displayRecentViews);

// top 스크롤 버튼
document.addEventListener("DOMContentLoaded", function () {
    const scrollTopBtn = document.getElementById("scrollTopBtn");

    // 버튼 클릭 시 최상단으로 부드럽게 스크롤
    scrollTopBtn.addEventListener("click", function () {
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    // 스크롤 위치에 따라 버튼 표시/숨김
    window.addEventListener("scroll", function () {
        if (window.scrollY > 0) { // 스크롤하면 버튼 표시
            scrollTopBtn.style.display = "block";
        } else {
            scrollTopBtn.style.display = "none";
        }
    });
});
