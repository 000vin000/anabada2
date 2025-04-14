document.getElementById("useCoin").addEventListener("click", function () {
	openUseCoinModal();
});

// 변동 내역 열기
function openUseCoinModal() {
	const token = localStorage.getItem('Token');
	
	if (!token) {
		alert('로그인이 필요합니다.');
		window.location.href = '/login';
		return;
	}
	
	getUseList();
	
	// 모달 열기
	document.getElementById("useCoinModal").style.display = "block";
}

// 데이터 받아오기
function getUseList() {
	const token = localStorage.getItem('Token');
	
	fetch('/api/coin/useCoinList', {
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
		showUseCoinModal(data);
	})
	.catch(error => {
		console.error('Fetch error: ', error);
	})
}

// 데이터 보여주기
function showUseCoinModal(data) {
	const tbody = document.querySelector("#useCoinModal tbody");
	
	tbody.innerHTML = '';
	console.log(data);
	
	if (Array.isArray(data.coinList)) {
		data.coinList.forEach(changeCoin => {
			const row = document.createElement("tr");
			const formattedChangeCoinAt = changeCoin.changecoinAt ? formatDate(changeCoin.changecoinAt) : '-';
			
			let typeColor = '-';
			switch (changeCoin.changecoinTypeKorean) {
			    case '코인 충전':
			    case '입찰 취소':
			    case '낙찰':
					typeColor = 'blue';
			        break; 
			    case '입찰':
			    case '수수료':
			    case '현금 전환':
					typeColor = 'red';
			        break; 
			}
			
			row.innerHTML = `
				<td>${formattedChangeCoinAt}</td> 
				<td>${changeCoin.changecoinTypeKorean}</td> 
				<td style="color: ${typeColor};">${changeCoin.changecoinAmount}</td>
				${changeCoin.itemNo ? `<td><a href="/item/detail/${changeCoin.itemNo.itemNo}>${changeCoin.itemNo.itemTitle}</a></td>` : '<td>-</td>'}
			`;
			tbody.appendChild(row);
		});
	} else {
		console.error("data.useCoinList is not an array", data);
	}
}

// 모달 닫기
window.onclick = function(event) {
	if (event.target === document.getElementById("useCoinModal")) {
		closeUseCoinModal();
	}
}

function closeUseCoinModal() {
	document.getElementById("useCoinModal").style.display = "none";
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