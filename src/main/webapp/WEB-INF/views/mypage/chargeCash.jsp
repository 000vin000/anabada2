<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>충전</title>
</head>
<body>
	<form id="chargeForm" method="post" action="/submitCharge" onsubmit="return handleSubmit(event)">
		<label><input type="radio" name="chargetype" value="nopassbook" checked onclick="updateForm()">무통장 입금</label>
		<label><input type="radio" name="chargetype" value="card" onclick="updateForm()">카드</label>
		
		<!-- 무통장 입금 폼 -->
		<div id="nopassbookForm">
			<h2>무통장 입금</h2>
			<label for="depositorName">입금자명</label>
			<input type="text" id="depositorName" name="depositorName"><br>
			<label for="amount">충전 금액</label>
			<input type="number" id="amount" name="amount"><br>
		</div>

		<!-- 카드 결제 폼 -->
		<div id="cardForm" style="display: none;">
			<h2>카드 결제</h2>
			<label for="cardNumber">카드번호</label>
			<input type="text" id="cardNumber" name="cardNumber"><br>
			<label for="cardAmount">충전 금액</label> 
			<input type="number" id="cardAmount" name="cardAmount"><br>
		</div>

		<input type="submit" value="확인">
	</form>
</body>

<script>
    window.onload = function() {
        var token = localStorage.getItem("Token");  // 로컬 스토리지에서 JWT 토큰을 가져옵니다.

        if (token) {
            // 페이지 로딩 시 서버에 토큰을 보내는 요청을 할 수 있습니다.
            fetch('/chargeCash', {
                method: 'GET',  // 또는 POST 등 필요한 메소드로 변경
                headers: {
                    'Authorization': 'Bearer ' + token,  // 토큰을 'Bearer' 타입으로 헤더에 추가
                    'Content-Type': 'application/json'  // 필요한 경우 Content-Type도 추가
                }
            })
            .then(response => response.json())
            .then(data => {
                console.log('응답 데이터:', data);
            })
            .catch(error => {
                console.error('에러 발생:', error);
            });
        } else {
            console.log('토큰이 존재하지 않습니다.');
        }
    }

    // 폼 전환
    function updateForm() {
        var selectedValue = document.querySelector('input[name="chargetype"]:checked').value;
        document.getElementById("nopassbookForm").style.display = selectedValue === "nopassbook" ? "block" : "none";
        document.getElementById("cardForm").style.display = selectedValue === "card" ? "block" : "none";
    }

    // 폼 제출 
    function handleSubmit(event) {        
        event.preventDefault(); // 기본 폼 제출 방지

        var selectedValue = document.querySelector('input[name="chargetype"]:checked').value;
        var amountField = selectedValue === "nopassbook" ? document.getElementById("amount") : document.getElementById("cardAmount");

        if (!amountField || !amountField.value) {
            alert("충전 금액을 입력해 주세요.");
            return false; // 폼 제출 방지
        }

        // 로컬 스토리지에서 JWT 토큰 가져오기
        var token = localStorage.getItem("Token");

        // Ajax 요청을 통해 서버에 폼 데이터 전송
        var formData = new FormData(document.getElementById("chargeForm"));

        fetch('/submitCharge', {
            method: 'POST',
            body: formData,
            headers: {
                'Authorization': 'Bearer ' + token,  // JWT 토큰을 헤더에 포함
            }
        })
        .then(response => response.json())  // 서버 응답을 JSON으로 받음
        .then(data => {
            // data가 객체일 경우, 그 안에 메시지를 정확히 꺼내서 alert로 표시
            alert(data.message || "충전 실패");  // 응답에 'message' 키를 포함시켜 메시지를 전달
            setTimeout(function() {
                document.getElementById("chargeForm").submit();
                window.close();
            }, 500);
        })
        .catch(error => {
            alert("충전 실패: " + error);
        });

        return false; 
    }
</script>

</html>
