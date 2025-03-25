// 최근 본 아이템 추가 (날짜별 저장 + 최신 날짜가 먼저 오도록 정렬)
function addRecentItem(itemNo, itemTitle) {
    const imageUrl = `http://192.168.0.41:8080/image/${itemNo}`;
    const dateKey = new Date().toISOString().split('T')[0]; // YYYY-MM-DD
    const newItem = { item_no: itemNo, item_title: itemTitle, image_url: imageUrl, date: dateKey };

    let recentItems = JSON.parse(localStorage.getItem(RECENT_ITEMS_KEY)) || {};

    // 모든 날짜의 아이템을 하나의 배열로 합침
    let allItems = Object.values(recentItems).flat();

    // 중복 제거 후 최신 아이템을 맨 앞에 추가
    allItems = allItems.filter(item => item.item_no !== newItem.item_no);
    allItems.unshift(newItem); // 최신 아이템이 앞에 오도록 추가

    // 최대 30개까지만 유지
    if (allItems.length > 30) {
        allItems = allItems.slice(0, 30);
    }

    // 날짜별 그룹화 후 최신 날짜가 먼저 오도록 정렬
    recentItems = sortObjectByKeyDesc(groupByDate(allItems));
    localStorage.setItem(RECENT_ITEMS_KEY, JSON.stringify(recentItems));
}

// 날짜별 그룹화
function groupByDate(items) {
    return items.reduce((acc, item) => {
        if (!acc[item.date]) acc[item.date] = [];
        acc[item.date].push(item);
        return acc;
    }, {});
}

// 객체 키(날짜)를 내림차순 정렬하는 함수 (최신 날짜가 먼저 오도록)
function sortObjectByKeyDesc(obj) {
    return Object.fromEntries(Object.entries(obj).sort(([a], [b]) => b.localeCompare(a)));
}

// URL에서 itemNo 가져오기
function getItemNoFromURL() {
    const pathParts = window.location.pathname.split('/');
    return pathParts[pathParts.length - 1];
}

// 페이지 로딩 시 자동 실행
document.addEventListener('DOMContentLoaded', () => {
    const itemNo = getItemNoFromURL();
    const itemTitleElement = document.querySelector(".item-name");
    if (itemNo && itemTitleElement) {
        const itemTitle = itemTitleElement.textContent.trim();
        addRecentItem(itemNo, itemTitle);
    }
});
