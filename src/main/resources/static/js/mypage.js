document.addEventListener("DOMContentLoaded", function () {
	loadCurrentCashCoin();
});

// 현재 사용자 잔액 확인
function loadCurrentCashCoin() {
	fetch("/api/coin", {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${localStorage.getItem("Token")}`,
		}
	}).then(response => response.json())
	.then(data => {
		const currentCash = document.getElementById("currentCash");
		const currentCoin = document.getElementById("currentCoin");
		currentCash.innerHTML = "";
		if (data.length != 0) {
			console.log(data);
			currentCash.innerHTML = "<p>보유 금액 : " + `${data.goods.goodsCash}` + " 원</p>";
			currentCoin.innerHTML = "<p>보유 코인 : " + `${data.goods.goodsCoin}` + " 개</p>";
			return;
		}
	}).catch(error => console.error("사용자 받아오기 실패:", error)); 
}