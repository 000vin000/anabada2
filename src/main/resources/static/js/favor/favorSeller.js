document.addEventListener("DOMContentLoaded", function () {
    const favBtn = document.getElementById("favor-btn");
    const sellerUserNo = favBtn.dataset.sellerNo;
    const favImg = document.createElement("img");
    favImg.alt = "☆";
   	favBtn.innerHTML = "";
    favBtn.appendChild(favImg);
    
    function updateFavoriteUI(isFavorite) {
		favImg.src = isFavorite ? "/images/favor-star-filled.png" : "/images/favor-star-empty.png";
    }
	
	fetch(`/api/favor/seller/${sellerUserNo}`, {
		method: "GET",
	    headers: {
	        "Authorization": `Bearer ${localStorage.getItem("Token")}`,
	    }})
		.then(res => res.json())
   		.then(data => {
   			updateFavoriteUI(data.isFavorite);
   	})
    
    async function toggleFavorite() {
        const response = await fetch(`/api/favor/seller/${sellerUserNo}`, { 
			method: "POST", 
			headers: {
	        	"Authorization": `Bearer ${localStorage.getItem("Token")}`,
		    }});
        if (response.status === 401) {
			if (confirm("로그인이 필요한 서비스입니다.\n로그인 하시겠습니까?")) {
	            window.location.href = "/auth/login.html"; // 로그인 페이지 경로로 이동
	        }
        } else {
	        const isFavorited = await response.json();
	        updateFavoriteUI(isFavorited);
        }
    }

    favBtn.addEventListener("click", toggleFavorite);
}); // jhu
