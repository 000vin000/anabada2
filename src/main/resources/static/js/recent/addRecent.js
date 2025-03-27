// 최근 본 아이템 추가 (날짜별로 저장)
function addRecentItem(itemNo, itemTitle) {
    const imageUrl = `http://192.168.0.41:8080/image/${itemNo}`;
    const dateKey = new Date().toISOString().split('T')[0]; // YYYY-MM-DD
    const newItem = { item_no: itemNo, item_title: itemTitle, image_url: imageUrl };

    let recentItems = JSON.parse(localStorage.getItem(RECENT_ITEMS_KEY)) || {};
    if (!recentItems[dateKey]) {
        recentItems[dateKey] = [];
    }

    // 중복 제거 및 최신 추가
    recentItems[dateKey] = recentItems[dateKey].filter(item => item.item_no !== itemNo);
    recentItems[dateKey].unshift(newItem);

    // 전체 개수 제한 (30개 유지)
    let allItems = Object.values(recentItems).flat();
    if (allItems.length > 30) {
        allItems = allItems.slice(0, 30);
        recentItems = groupByDate(allItems);
    }

    localStorage.setItem(RECENT_ITEMS_KEY, JSON.stringify(recentItems));
}

// 날짜별 그룹화
function groupByDate(items) {
    return items.reduce((acc, item) => {
        const dateKey = new Date().toISOString().split('T')[0];
        if (!acc[dateKey]) acc[dateKey] = [];
        acc[dateKey].push(item);
        return acc;
    }, {});
}

// URL에서 itemNo 가져오기
function getItemNoFromURL() {
    const pathParts = window.location.pathname.split('/'); // URL 경로를 '/' 기준으로 분리
    return pathParts[pathParts.length - 1]; // 마지막 요소가 itemNo
}

// 페이지 로딩 시 자동 실행
document.addEventListener('DOMContentLoaded', () => {
    const itemNo = getItemNoFromURL();
    const itemTitleElement = document.querySelector(".item-name");
    if (itemNo && itemTitleElement) {
        const itemTitle = itemTitleElement.textContent.trim(); // 제목 가져오기
        addRecentItem(itemNo, itemTitle);
    }
});