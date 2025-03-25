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
	
	fetch(`/api/favor/item/${itemNo}`)
		.then(res => res.json())
   		.then(data => {
   			updateFavoriteUI(data.isFavorite);
   	})
    
    async function toggleFavorite() {
        const response = await fetch(`/api/favor/item/${itemNo}`, { method: "POST" });
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
        } else {
	        const isFavorited = await response.json();
	        updateFavoriteUI(isFavorited);
        }
    }

    favBtn.addEventListener("click", toggleFavorite);
}); // jhu
