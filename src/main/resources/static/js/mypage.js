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
		if (data.length != 0) {
			const formattedCash = Number(data.goods.goodsCash).toLocaleString(); 
			const formattedCoin = Number(data.goods.goodsCoin).toLocaleString();
			currentCash.innerHTML += `<span>${formattedCash}` + " 원</span>";
			currentCoin.innerHTML += `<span>${formattedCoin}` + " 코인</span>";
			return;
		}
	}).catch(error => console.error("사용자 받아오기 실패:", error)); 
}