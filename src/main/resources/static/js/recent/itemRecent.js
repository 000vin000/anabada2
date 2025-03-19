// 아래는 현재 외부 js 파일채로 사이드바가 있는 페이지에 적용하기
// JavaScript로 로컬스토리지에 상품 정보 저장
function addRecentView(itemNo, itemName, itemImage) {
    // const maxItems = 5; // 최대 저장 개수
    const storageKey = "recentItems";
	
	const expiryTime = 7 * 24 * 60 * 60 * 1000; // 7일 (밀리초 단위)
	const now = new Date().getTime();

    // 기존 로컬스토리지 값 가져오기
    let recentItems = JSON.parse(localStorage.getItem(storageKey)) || [];
	recentItems = recentItems.filter(item => item.expiry > now); // 유효한 데이터만 남김

    // 중복 제거 (이미 있는 상품이면 삭제)
    recentItems = recentItems.filter(item => item.id !== itemNo);

    // 새로운 상품 추가
	recentItems.unshift({ id: itemNo, name: itemName, image: itemImage, expiry: now + expiryTime });

    // 최대 개수 초과 시 마지막 요소 제거
    // if (recentItems.length > maxItems) {
    //     recentItems.pop();
    // }

    // 로컬스토리지에 저장
    localStorage.setItem(storageKey, JSON.stringify(recentItems));
	
	displayRecentViews();
}

// 최근 본 상품 목록을 화면에 표시하는 함수
function displayRecentViews() {
    const storageKey = "recentItems";
	const now = new Date().getTime(); // 현재 시간
	let recentItems = JSON.parse(localStorage.getItem(storageKey)) || [];
    // 만료된 항목 제거
    recentItems = recentItems.filter(item => item.expiry > now);
	// 변경된 목록을 다시 저장 (만료된 항목 제거 반영)
    localStorage.setItem(storageKey, JSON.stringify(recentItems));
	
	const container = document.getElementById("recently-viewed");
	if (!container) {
		return;
	}

    container.innerHTML = ""; // 기존 목록 초기화

    // recentItems.forEach(item => {
    //     const itemElement = document.createElement("div");
    //     itemElement.classList.add("recent-item");

    //     itemElement.innerHTML = `
	// 		<a href="/recentItem">
	//             <img src="${item.image}" alt="${item.name}" width="50" height="50">
	// 		</a>
    //     `;

    //     container.appendChild(itemElement);
    // }); // 기존 반복문을 통해 보여주던거

    const item = recentItems[0]; // 첫 번째 아이템만 가져오기

    if (item) {
        // 배열이 비어있지 않은 경우 실행
        const itemElement = document.createElement("div");
        itemElement.classList.add("recent-item");

        itemElement.innerHTML = `
            <a href="/recentItem">
                <img src="http://localhost:8080/images/${item.itemNo}" alt="recent" width="50" height="50">
            </a>
        `;

        container.appendChild(itemElement);
    }

}

// 페이지 로드 시 최근 본 상품 목록 표시
document.addEventListener("DOMContentLoaded", displayRecentViews);

// 적용 방법은 sidebar.jsp 참고

document.addEventListener("DOMContentLoaded", function () {
    const scrollTopBtn = document.getElementById("scrollTopBtn");

    // 버튼 클릭 시 최상단으로 부드럽게 스크롤
    scrollTopBtn.addEventListener("click", function () {
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    // 스크롤 위치에 따라 버튼 표시/숨김
    window.addEventListener("scroll", function () {
        if (window.scrollY > 200) { // 200px 이상 스크롤하면 버튼 표시
            scrollTopBtn.style.display = "block";
        } else {
            scrollTopBtn.style.display = "none";
        }
    });
});
