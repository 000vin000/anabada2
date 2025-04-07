<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>현금으로 전환하기</title>
</head>
<body>
	<form id="toCash" method="post" action="/changeChargeToCash" onsubmit="return toCashSubmit(event)">
		<!-- 보유중인 코인 표시 -->
		<div id="currentCoin"></div>
		
		<!-- 전환할 코인 -->
		<div>
			<p>전환할 코인</p>
			<input type="number" id="changeCoinToCash" name="changeCoinToCash" min="1000" step="1000">
			<input type="submit" value="전환 신청">
		</div>
	</form>
</body>
<script src="../js/coinToCash.js"></script>
<script>
document.addEventListener("DOMContentLoaded", function () {
	const token = localStorage.getItem("Token");
	
	if (!token) {
    	alert("로그인이 필요합니다.");
    	window.close();
    	return;
    }
	
	loadCurrentCoin();
});

//음수 및 소수점 입력 방지
document.getElementById("changeCoinToCash").addEventListener("input", function () {
    this.value = Math.max(0, Math.floor(this.value)); // 소수점 제거 및 음수 방지
});

// 폼 제출
function toCashSubmit(event) {
	event.preventDefault();
	
	let amount = document.getElementById("changeCoinToCash").value;
    if (amount <= 0) {
        alert("전환할 금액은 1000원 이상이어야 합니다.");
        return false;
    }

    let token = localStorage.getItem("Token");
    let formData = new FormData(document.getElementById("toCash"));
    
    fetch('/changeChargeToCash', {
        method: 'POST',
        body: formData,
        headers: {
            'Authorization': 'Bearer ' + token,
        }
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message || "전환 신청 실패");
        setTimeout(function() {
            document.getElementById("toCash").submit();
            window.close();
        }, 500);
    })
    .catch(error => {
        alert("전환 실패: " + error);
    });

    return true;
}
</script>
</html>