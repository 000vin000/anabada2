document.getElementById("chargeList").addEventListener("click", function () {
	openChargeListModal();
});

// 충전 내역 열기
function openChargeListModal() {
	const token = localStorage.getItem('Token');
		
	if (!token) {
		alert('로그인이 필요합니다.');
		window.location.href = '/login';
		return;
	}

	getList();

	// 모달 열기
	document.getElementById("chargeListModal").style.display = "block";
}

// 데이터 받아오기
function getList() {
	const token = localStorage.getItem('Token');
	
	fetch('/api/coin/chargeList', {
		method: 'GET',
		headers: {
			'Authorization': `Bearer ` + token,
		}
	})
	.then(response => {
		if (!response.ok) {
			throw new Error('Network response was not ok');
		}
		return response.json();
	})
	.then(data => {
		populateChargeHistory(data);
	})
	.catch(error => {
		console.error('Fetch error:', error);
	});
}

// 날짜 형식
function formatDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더해야 합니다.
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// 데이터 보여주기
function populateChargeHistory(data) {
	const tbody = document.querySelector("#chargeListModal tbody");
	
	tbody.innerHTML = '';
	console.log(data);
	
	if (Array.isArray(data.chargeList)) {
		data.chargeList.forEach(account => {
			const row = document.createElement("tr");
			const status = account.accountAt ? '충전 완료' : '확인중';
			
			const formattedAccountAt = formatDate(account.accountReqAt);
			const accountTypeKor = account.accountPayType == "NOPASSBOOK" ? "무통장 입금" : "카드결제";
			
			row.innerHTML = `
				<td>${formattedAccountAt}</td>
				<td>${account.accountAmount}</td>
				<td>${accountTypeKor}</td>
				<td>${status}</td>
			`;
			tbody.appendChild(row);
		});
	} else {
		console.error("data.chargeList is not an array", data);
	}
}

// 모달 닫기
window.onclick = function(event) {
	if (event.target == document.getElementById("chargeListModal")) {
		closeChargeListModal();
	}
}

// 충전 내역 닫기
function closeChargeListModal() {
	document.getElementById("chargeListModal").style.display = "none";
}


















