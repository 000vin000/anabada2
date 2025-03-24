document.addEventListener("DOMContentLoaded", () => {
    renderRecentItems();
    document.getElementById("delete-recent").addEventListener("click", toggleDeleteMode);
});

// 최근 본 목록 가져오기
function getRecentItems() {
    return JSON.parse(localStorage.getItem(RECENT_ITEMS_KEY)) || {};
}

let deleteMode = false;

function renderRecentItems() {
    const recentItems = getRecentItems();
    const recentListContainer = document.getElementById("recent-items");
    
    if (!recentListContainer) return;
    recentListContainer.innerHTML = ""; // 기존 목록 초기화

    if (deleteMode) {
        const deleteControls = document.createElement("div");
        deleteControls.id = "delete-controls";
        
        const selectAllCheckbox = document.createElement("input");
        selectAllCheckbox.type = "checkbox";
        selectAllCheckbox.id = "select-all";
        selectAllCheckbox.addEventListener("change", toggleSelectAll);
        
        const selectAllLabel = document.createElement("label");
        selectAllLabel.textContent = "전체 선택";
        selectAllLabel.setAttribute("for", "select-all");
        
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "선택 삭제";
        deleteButton.id = "confirm-delete";
        deleteButton.addEventListener("click", deleteSelectedItems);
        
        deleteControls.appendChild(selectAllCheckbox);
        deleteControls.appendChild(selectAllLabel);
        deleteControls.appendChild(deleteButton);
        
        recentListContainer.appendChild(deleteControls);
    }

    Object.keys(recentItems).sort((a, b) => new Date(b) - new Date(a)) // 날짜 최신순 정렬
        .forEach(date => {
            const dateHeader = document.createElement("h3");
            dateHeader.textContent = date;
            recentListContainer.appendChild(dateHeader);

            const ul = document.createElement("ul");
            ul.classList.add("recent-items-list");
            
            recentItems[date].forEach(item => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <label>
                        <input type="checkbox" class="delete-checkbox" data-date="${date}" data-item-no="${item.item_no}" style="display: none;">
                        <a href="/item/detail/${item.item_no}" class="recent-item">
                            <img src="${item.image_url}" alt="${item.item_title}" width="50">
                            <span>${item.item_title}</span>
                        </a>
                    </label>
                `;
                ul.appendChild(li);
            });
            recentListContainer.appendChild(ul);
        });
}

function toggleDeleteMode() {
    deleteMode = !deleteMode;
    renderRecentItems();
    const checkboxes = document.querySelectorAll(".delete-checkbox");
    checkboxes.forEach(checkbox => {
        checkbox.style.display = deleteMode ? "inline-block" : "none";
    });
}

function toggleSelectAll(event) {
    const checked = event.target.checked;
    document.querySelectorAll(".delete-checkbox").forEach(checkbox => {
        checkbox.checked = checked;
    });
}

function deleteSelectedItems() {
    let recentItems = JSON.parse(localStorage.getItem(window.RECENT_ITEMS_KEY)) || {};
    const selectedCheckboxes = document.querySelectorAll(".delete-checkbox:checked");
    
    selectedCheckboxes.forEach(checkbox => {
        const date = checkbox.getAttribute("data-date");
        const itemNo = checkbox.getAttribute("data-item-no");
        if (recentItems[date]) {
            recentItems[date] = recentItems[date].filter(item => item.item_no !== itemNo);
            if (recentItems[date].length === 0) delete recentItems[date];
        }
    });
    
    localStorage.setItem(window.RECENT_ITEMS_KEY, JSON.stringify(recentItems));
    renderRecentItems();
    toggleDeleteMode();
}
