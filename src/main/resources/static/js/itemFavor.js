// 상세페이지에 들어갈 스크립트 jhu
// 상세페이지에서 사용할 즐겨찾기 관련 기능과 최근본 목록에 필요한 기능이 포함되어있음

// 상세페이지에 들어갔을때 경로에 있는 아이템 넘버를 가져오는 동작
document.addEventListener("DOMContentLoaded", () => {
	let itemNo = window.location.pathname.split('/').pop();
	itemNo = isNaN(itemNo) ? null : Number(itemNo);
	// 상품상세페이지로 갔을때 넘버 가져오기, 숫자로 변환 & 예외 처리
	// /로 배열을 나누고 마지막 요소를 itemNo에 저장함
	const itemNameElement = document.querySelector(".item-name");
	const itemName = itemNameElement ? itemNameElement.innerText : "";
	// 이름을 가져오려고 이너텍스트를 훔쳐오는 부분
	const itemImageUrl = itemNo ? `http://localhost:8080/images/${itemNo}` : null;
	// itemNo를 통해 이미지경로를 기억함
	
	if (itemNo) {
		addRecentView(itemNo, itemName, itemImageUrl);
	}
}); // jhu

// 상세페이지의 즐겨찾기 버튼을 눌렀을때 즐겨찾기 상호작용을 하는 동작
document.addEventListener("DOMContentLoaded", function () {
    const favBtn = document.getElementById("favor-btn");
    const itemNo = favBtn.dataset.itemNo;
    const favImg = document.createElement("img");
    favImg.alt = "☆";
   	favBtn.innerHTML = "";
    favBtn.appendChild(favImg);
    
    function updateFavoriteUI(isFavorite) {
		favImg.src = isFavorite ? "/images/favor-star-filled.png" : "/images/favor-star-empty.png";
    }
	
	fetch(`/api/favor/${itemNo}`)
		.then(res => res.json())
   		.then(data => {
   			updateFavoriteUI(data.isFavorite);
   	})
    
    async function toggleFavorite() {
        const response = await fetch(`/api/favor/${itemNo}`, { method: "POST" });
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
        } else {
	        const isFavorited = await response.json();
	        updateFavoriteUI(isFavorited);
        }
    }

    favBtn.addEventListener("click", toggleFavorite);
}); // jhu