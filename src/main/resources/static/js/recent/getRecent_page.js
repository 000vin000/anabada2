// 최근 본 목록 가져오기
function getRecentItems() {
    return JSON.parse(localStorage.getItem(RECENT_ITEMS_KEY)) || {};
}

// 개별 삭제
function removeItem(date, itemNo) {
    let recentItems = getRecentItems();
    if (recentItems[date]) {
        recentItems[date] = recentItems[date].filter(item => item.item_no !== itemNo);
        if (recentItems[date].length === 0) delete recentItems[date];
    }
    localStorage.setItem(RECENT_ITEMS_KEY, JSON.stringify(recentItems));
}

// 선택된 아이템 일괄 삭제
function removeSelectedItems(selectedItems) {
    let recentItems = getRecentItems();
    selectedItems.forEach(({ date, itemNo }) => {
        if (recentItems[date]) {
            recentItems[date] = recentItems[date].filter(item => item.item_no !== itemNo);
            if (recentItems[date].length === 0) delete recentItems[date];
        }
    });
    localStorage.setItem(RECENT_ITEMS_KEY, JSON.stringify(recentItems));
}

// UI 렌더링 (예제 코드, 실제로는 React/Vue 등 활용 가능)
function renderRecentItems() {
    const recentItems = getRecentItems();
    let html = "";
    Object.keys(recentItems).forEach(date => {
        html += `<h3>${date}</h3><ul>`;
        recentItems[date].forEach(item => {
            html += `<li>${item.item_title} <button onclick="removeItem('${date}', ${item.item_no})">삭제</button></li>`;
        });
        html += `</ul>`;
    });
    document.getElementById("recent-items").innerHTML = html;
}

// 페이지 로드 시 목록 표시
document.addEventListener("DOMContentLoaded", renderRecentItems);
