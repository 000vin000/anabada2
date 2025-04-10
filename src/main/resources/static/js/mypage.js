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
		if (data.length === 0) return;
		
		const { user, goodsCash, goodsCoin } = data.goods;
		const formattedCash = Number(goodsCash).toLocaleString();
		const formattedCoin = Number(goodsCoin).toLocaleString();
		
		const formattedDate = formatDate(user.userCreatedDate);
		const userInfoHTML = `
			<div><span class="userInfoLabel">가입 일자</span><span class="userInfoValue">${formattedDate}</span></div>
			<div><span class="userInfoLabel">이름</span><span class="userInfoValue">${user.userName}</span></div>
			<div><span class="userInfoLabel">이메일</span><span class="userInfoValue">${user.userEmail}</span></div>
			<div><span class="userInfoLabel">전화번호</span><span class="userInfoValue">${user.userPhone}</span></div>
		`;
		
		document.getElementById("user-name").innerHTML = `<h2>${user.userNick}(${user.userId})</h2>`;
		document.getElementById("user-info").innerHTML = userInfoHTML;
		document.getElementById("currentCash").innerHTML += `<span>${formattedCash} 원</span>`;
		document.getElementById("currentCoin").innerHTML += `<span>${formattedCoin} 코인</span>`;
	}).catch(error => console.error("사용자 받아오기 실패:", error)); 
}

// 날짜 형식 변환 
function formatDate(isoString) {
  const date = new Date(isoString);
  const year = date.getFullYear();
  const month = ('0' + (date.getMonth() + 1)).slice(-2);
  const day = ('0' + date.getDate()).slice(-2);
  const hour = ('0' + date.getHours()).slice(-2);
  const minute = ('0' + date.getMinutes()).slice(-2);
  return `${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분`;
}