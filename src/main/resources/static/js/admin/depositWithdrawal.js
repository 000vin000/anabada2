document.addEventListener("DOMContentLoaded", function () {
	checkAdminAndGetAccountList();
});

// 관리자 검증
function checkAdminAndGetAccountList() {
    const token = localStorage.getItem("Token");

    if (!token) {
        window.location.href = "/error/noAdmin"; 
        return;
    }

    fetch("/api/cash/depositWithdrawalList", {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
        }
    })
    .then(response => {
        if (response.status === 401) {
            window.location.href = "/error/noAdmin"; 
            return;
        }
        if (response.status === 204) {
            window.location.href = "/error/noAdmin"; 
            return;
        }
        return response.json();
    })
    .then(data => {
        if (data) {
            console.log(data);
            showDepositList(data);
            showWithdrawalList(data);
        }
    })
    .catch(error => {
        console.error("Fetch error: ", error);
    });
}

// 입금 내역
function showDepositList(data) {
	const tbody = document.querySelector("#forDepositList tbody");
	tbody.innerHTML = "";
	data.accountList
		.filter(account => account.accountType === "DEPOSIT")
		.forEach(account => {
			const row = document.createElement("tr");
			
			row.innerHTML = `
				<td>${account.accountNo}</td>
				<td>${formatDate(account.accountReqAt)}</td>
				<td>${account.userNo?.userId || "알 수 없음"}</td>
				<td>${account.accountAmount}</td>
				<td><button onclick="acceptAccount(${account.accountNo})">확인</button></td>
			`;
			
			tbody.appendChild(row);
		});
}

// 출금 내역
function showWithdrawalList(data) {
	const tbody = document.querySelector("#forWithdrawalList tbody");
	tbody.innerHTML = "";

	data.accountList
		.filter(account => account.accountType === "WITHDRAWAL")
		.forEach(account => {
			const row = document.createElement("tr");
			
			row.innerHTML = `
				<td>${account.accountNo}</td>
				<td>${formatDate(account.accountReqAt)}</td>
				<td>${account.userNo?.userId || "알 수 없음"}</td>
				<td>${account.accountAmount}</td>
				<td><button onclick="acceptAccount(${account.accountNo})">확인</button></td>
			`;
			
			tbody.appendChild(row);
		});
}

// 확인 처리
function acceptAccount(accountNo) {
    const token = localStorage.getItem("Token");
	
	fetch(`/api/cash/acceptAccount/${accountNo}`, {
		method: "POST",
		headers: {
		    "Authorization": `Bearer ${token}`,
		    "Content-Type": "application/json"
		}
	})
	.then(response => {
	        if (!response.ok) {
	            throw new Error("입출금 확인 실패");
	        }
	        return response.json();
	    })
	    .then(data => {
	        alert(data.message);
	        location.reload();
	    })
	    .catch(error => {
	        console.error("입출금 확인 에러:", error);
	    });
}

// ✅ 날짜 형식 변환 함수
function formatDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}