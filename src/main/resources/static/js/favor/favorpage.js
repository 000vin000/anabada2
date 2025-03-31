document.addEventListener("DOMContentLoaded", function () {
    loadFavorItems();
    loadFavorSellers();
});

function showTab(tabName) {
    document.querySelectorAll(".content").forEach(content => content.classList.remove("active"));
    document.querySelectorAll(".tab").forEach(tab => tab.classList.remove("active"));
    
    document.getElementById(tabName).classList.add("active");
    document.querySelector(`.tab[onclick="showTab('${tabName}')"]`).classList.add("active");
}

// 관심물품 불러오기
function loadFavorItems() {
    fetch("/api/favor/itemlist"	, {
		method: "GET",
	    headers: {
	        "Authorization": `Bearer ${localStorage.getItem("Token")}`,
	    }}) // 백엔드 API 호출
        .then(response => response.json())
        .then(data => {
            const itemList = document.getElementById("itemList");
            itemList.innerHTML = ""; // 기존 목록 초기화
            if (data.length === 0) {
                itemList.innerHTML = "<li>관심물품이 없습니다.</li>";
                return;
            }
            data.forEach(item => {
                const li = document.createElement("li");
                li.innerHTML = `
					<a href="/item/detail/${item.item.itemNo}">
                        <img src="http://192.168.0.41:8080/image/${item.item.itemNo}" alt="${item.item.itemTitle}" width="50">
                        <span>${item.item.itemTitle}</span>
                    </a>
                    <button onclick="removeFavorItem(${item.item.itemNo})">삭제</button>
                `;
                itemList.appendChild(li);
            });
        })
        .catch(error => console.error("관심물품 불러오기 실패:", error));
}

// 관심판매자 불러오기
function loadFavorSellers() {
    fetch("/api/favor/sellerlist", {
		method: "GET",
	    headers: {
	        "Authorization": `Bearer ${localStorage.getItem("Token")}`,
	    }}) // 백엔드 API 호출
        .then(response => response.json())
        .then(data => {
            const sellerList = document.getElementById("sellerList");
            sellerList.innerHTML = ""; // 기존 목록 초기화
            if (data.length === 0) {
                sellerList.innerHTML = "<li>관심판매자가 없습니다.</li>";
                return;
            }
            data.forEach(seller => {
	            const li = document.createElement("li");
	            li.innerHTML = `
					<a href="/user/profile/${seller.seller.user.userNo}">
		                <span>${seller.seller.user.userNick}</span> 
					</a>
	                <button onclick="removeFavorSeller(${seller.seller.sellerNo})">삭제</button>
	            `;
				sellerList.appendChild(li);
            });
        })
        .catch(error => console.error("관심판매자 불러오기 실패:", error));
}

function removeFavorItem(itemNo) {
	if(!confirm("삭제하시겠습니까?")) {
		return;
	}
	
	fetch("/api/favor/item/" + itemNo, {
		method : "delete",
	    headers: {
	        "Authorization": `Bearer ${localStorage.getItem("Token")}`,
	    }})
		.then(response => response.text())
		.then(data => {
			alert(data); // 삭제 결과
			location.reload(); // 페이지 리로드
		}).catch(error => console.error("삭제 실패 : ", error));
}

function removeFavorSeller(sellerNo) {
	if(!confirm("삭제하시겠습니까?")) {
		return;
	}
	
	fetch("/api/favor/seller/" + sellerNo, {
		method : "delete",
	    headers: {
	        "Authorization": `Bearer ${localStorage.getItem("Token")}`,
	    }})
		.then(response => response.text())
		.then(data => {
			alert(data); // 삭제 결과
			location.reload(); // 페이지 리로드
		}).catch(error => console.error("삭제 실패 : ", error));
}