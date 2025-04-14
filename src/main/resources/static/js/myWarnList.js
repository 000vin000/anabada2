document.addEventListener("DOMContentLoaded", function() {
	getMyWarnList();
});

function getMyWarnList() {
	fetch("/api/myWarnList", {
		method: "GET", 
		headers: {
			"Authorization" : `Bearer ${localStorage.getItem("Token")}`
		}
	}).then(response => {
		if (!response.ok) {
			throw new Error('Network response was not ok');
		}
		return response.json();
	}).then(data => {
		showWarnList(data);
	}).catch(error => {
		console.error('Fetch error: ', error);
	});
}

function showWarnList(data) {
	const tbody = document.querySelector(".warnTable tbody");
	
	tbody.innerHTML = '';
	
	const whereMap = {
		PROFILE: "프로필",
		ITEM: "아이템",
		CHATTING: "채팅"
	};
	
	const reasonMap = {
		SPAM: "스팸홍보/도배글",
		PORNOGRAPHY: "음란물",
		ILLEGALITY: "불법정보 포함",
		HARM: "청소년에게 유해한 내용",
		ABUSE: "욕설/비방 표현",
		PRIVACY: "개인정보 노출 게시물"
	};
	
	const resultMap = {
		SUSPENSION: "정지",
		PERMANENTSTOP: "영구 정지"
	};
	if (Array.isArray(data.warns)) {
		data.warns.forEach(warn => {
			const row = document.createElement("tr");
			const formattedProcessedDate = formatDate(warn.warnProcessedDate);
			
			const where = whereMap[warn.warnWhere] || warn.warnWhere;
			const reason = warn.warnReason === 'OTHER' ? warn.warnReasonDetail : (reasonMap[warn.warnReason] || warn.warnReason);
			const result = resultMap[warn.warnResult] || warn.warnResult;
			
			let link = "#";
			if (warn.warnWhere === "PROFILE") {
				link = `/user/profile/${warn.warnDefendantUser}`;
			} else if (warn.warnWhere === "ITEM") {
				link = `/item/detail/${warn.warnItem.itemNo}`;
			} else if (warn.warnWhere === "CHATTING") {
				link = "#";
			}
			
			row.innerHTML = `
				<td>${warn.warnNo}</td>
				<td>${formattedProcessedDate}</td>
				<td>${reason}</td>				
				<td><a href="${link}" target="_blank">${where}</a></td>
				<td>${result} ${warn.warnSuspensionDays}일</td>
			`;
			tbody.appendChild(row);
		});
	} else {
		const divError = document.getElementById("warnError");
		divError.innerHTML = data.error;
	}
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