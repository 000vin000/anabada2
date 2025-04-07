// 출금 신청 submit
function withdrawal(event) {
	event.preventDefault();
	
	const cashField = document.getElementById("withdrawalCash");
	if (!cashField || !cashField.value) {
		alert("출금할 금액을 입력해 주세요.");
		return false;
	}
	
	const token = localStorage.getItem("Token");
	const formData = new FormData(document.getElementById("withdrawal"));
	
	fetch('/withdrawalCash', {
		method: 'POST',
		body: formData,
		headers: {
			'Authorization': 'Bearer ' + token,
		}
	}).then(response => response.json())
	.then(data => {
		alert(data.message);
		setTimeout(function() {
			document.getElementById("withdrawal").submit();
			window.close();
		}, 500);
	})
	.catch(error => {
		alert("충전 실패: " + error);
	});
	
	return false;
}