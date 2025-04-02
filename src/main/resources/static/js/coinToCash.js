function loadCurrentCoin() {
	fetch("/api/coin/cashToCoin", {
	    method: "GET",
	    headers: {
	        "Authorization": `Bearer ${localStorage.getItem("Token")}`,
	    }
	})
	.then(response => {
	    if (!response.ok) {
	        throw new Error("서버 응답 실패");
	    }
	    return response.json();
	})
	.then(data => {
	    const currentCoin = document.getElementById("currentCoin");
		currentCoin.innerHTML = "";
	    if (data && data.goods && data.goods.goodsCoin !== undefined) {
	        console.log("사용자 데이터:", data);
	        currentCoin.innerHTML = `<p>보유 코인 : ${data.goods.goodsCoin} 코인</p>`;
	    } else {
	        currentCoin.innerHTML = "<p>코인 정보를 불러올 수 없습니다.</p>";
	    }
	})
	.catch(error => {
	    console.error("사용자 데이터 로딩 실패: ", error);
	    document.getElementById("currentCoin").innerHTML = "<p>코인 정보를 불러올 수 없습니다.</p>";
	    alert("잔액 정보를 가져오는 중 오류가 발생했습니다.");
	});
}