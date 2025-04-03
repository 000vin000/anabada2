document.getElementById("withdrawalList").addEventListener("click", function () {
	openWithdrawalListModal();
});

// 출금 내역 열기
function openWithdrawalListModal() {
	const token = localStorage.getItem('Token');
	
	if (!token) {
		alert('로그인이 필요합니다.');
		window.location.href = '/login';
		return;
	}

	getWithdrawalList();

	// 모달 열기
	document.getElementById("withdrawalModal").style.display = "block";
}

// 데이터 받아오기
function getWithdrawalList() {
	const token = localStorage.getItem('Token');
	
	fetch('/api/coin/withdrawalList', {
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
		withdrawHistory(data); 
	})
	.catch(error => {
		console.error('Fetch error: ', error);
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
function withdrawHistory(data) {
	const tbody = document.querySelector("#withdrawalModal tbody");
	
	tbody.innerHTML = '';
	console.log(data);
	
	if (Array.isArray(data.withdrawalList)) {
		data.withdrawalList.forEach(data => {
			const row = document.createElement("tr");
			const status = data.accountAt ? '출금 완료' : '대기중';
			
			const formattedAccountReqAt = formatDate(data.accountReqAt);
			const formattedAccountAt = data.accountAt ? formatDate(data.accountAt) : '-';
			
			row.innerHTML = `
				<td>${formattedAccountReqAt}</td>
				<td>${data.accountAmount}</td>
				<td>${status}</td>
				<td>${data.accountBankForWithdraw} ${data.accountNumberForWithdraw}</td>
				<td>${formattedAccountAt}</td>
				${!data.accountAt ? `<td><button class="withdrawalCancel" data-account-no="${data.accountNo}">신청 취소</button></td>` : '<td>-</td>'}
			`;
			tbody.appendChild(row);
		});
		document.querySelectorAll('.withdrawalCancel').forEach(button => {
			button.addEventListener('click', function() {
				const accountNo = button.getAttribute('data-account-no');
				// 사용자 확인
				if (confirm("신청 내역이 삭제됩니다. 정말로 취소하시겠습니까?")) {
					cancelAccount(accountNo);
				}
			});
		});
	} else {
		console.error("data.withdrawalList is not an array", data);
	}
}

// 출금 취소 처리
function cancelAccount(accountNo) {
	const token = localStorage.getItem('Token');
	
	fetch(`/api/coin/cancelWithdrawal/${accountNo}`, {
		method: 'DELETE',
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
		alert(data.message || "취소 성공");
		closeWithdrawalModal();
	})
	.catch(error => {
		console.error('Cancel request error:', error);
	});
}

// 모달 닫기
window.onclick = function(event) {
	if (event.target == document.getElementById("withdrawalModal")) {
		closeWithdrawalModal();
	}
}

// 신청 내역 닫기
function closeWithdrawalModal() {
	document.getElementById("withdrawalModal").style.display = "none";
}