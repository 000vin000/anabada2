document.getElementById("toCoinList").addEventListener("click", function () {
	openConversionModal();
});

// 신청 내역 열기
function openConversionModal() {
	const token = localStorage.getItem('Token');
	
	if (!token) {
		alert('로그인이 필요합니다.');
		window.location.href = '/login';
		return;
	}

	fetchData();

	// 모달 열기
	document.getElementById("conversionModal").style.display = "block";
}

// 서버에서 데이터 받아오기
function fetchData() {
	const token = localStorage.getItem('Token');

	fetch('/api/coin/conversionList', {
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
		populateConversionHistory(data); 
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
function populateConversionHistory(data) {
	const tbody = document.querySelector("#conversionModal tbody");

	tbody.innerHTML = '';
	console.log(data);
	
	if (Array.isArray(data.conList)) {
		data.conList.forEach(conversion => {
			const row = document.createElement("tr");
			const status = conversion.conversionAt ? '전환 완료' : '대기중';
			
			const formattedConversionReqAt = formatDate(conversion.conversionReqAt);
			const formattedConversionAt = conversion.conversionAt ? formatDate(conversion.conversionAt) : '-';

			row.innerHTML = `
				<td>${formattedConversionReqAt}</td>
				<td>${conversion.conversionAmount}</td>
				<td>${status}</td>
				<td>${formattedConversionAt}</td>
				${!conversion.conversionAt ? `<td><button class="toCoinCancel" data-conversion-no="${conversion.conversionNo}">신청 취소</button></td>` : '<td></td>'}
			`;
			tbody.appendChild(row);
		});
		document.querySelectorAll('.toCoinCancel').forEach(button => {
			button.addEventListener('click', function(event) {
				const conversionNo = button.getAttribute('data-conversion-no');
				// 사용자 확인
				if (confirm("신청 내역이 삭제됩니다. 정말로 삭제하시겠습니까?")) {
					cancelConversion(conversionNo, button);
				}
			});
		});
	} else {
		console.error("data.conList is not an array", data);
	}
}

// 신청 취소 처리
function cancelConversion(conversionNo, button) {
	const token = localStorage.getItem('Token');

	fetch(`/api/coin/cancelConversion/${conversionNo}`, {
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
		alert(data.message || "취소 실패");
		closeConversionModal();
	})
	.catch(error => {
		console.error('Cancel request error:', error);
	});
}

// 모달 닫기
window.onclick = function(event) {
	if (event.target == document.getElementById("conversionModal")) {
		closeConversionModal();
	}
}

// 신청 내역 닫기
function closeConversionModal() {
	document.getElementById("conversionModal").style.display = "none";
}