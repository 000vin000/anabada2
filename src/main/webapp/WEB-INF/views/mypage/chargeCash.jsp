<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>충전</title>
</head>
<style>
body {
    font-family: 'Noto Sans KR', sans-serif;
    background-color: #f9f9f9;
    color: #333;
    margin: 40px;
    line-height: 1.6;
}

form {
    max-width: 400px;
    margin: auto;
    padding: 30px;
    background-color: white;
    border: 1px solid #ddd;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

label {
    display: block;
    margin-bottom: 15px;
    font-weight: 600;
    cursor: pointer;
}

input[type="text"],
input[type="number"] {
    width: 100%;
    padding: 10px;
    margin-top: 5px;
    margin-bottom: 15px;
    border: 1px solid #ccc;
    border-radius: 6px;
    box-sizing: border-box;
    font-size: 14px;
}

h2 {
    margin-top: 0;
    color: #007d8a;
    text-align: center;
    margin-bottom: 20px;
}

input[type="radio"] {
    margin-right: 8px;
    transform: scale(1.2);
}

/* 폼 내 구역 */
#nopassbookForm,
#cardForm {
    margin-bottom: 20px;
    padding: 15px;
    background-color: #fafafa;
    border: 1px solid #eee;
    border-radius: 8px;
}

/* 제출 버튼 */
input[type="submit"] {
    width: 100%;
    background-color: white;
    color: #007d8a;
    border: 2px solid #007d8a;
    padding: 12px;
    border-radius: 6px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
    font-size: 16px;
}

input[type="submit"]:hover {
    background-color: #007d8a;
    color: white;
    transform: scale(1.03);
}

/* 반응형 */
@media (max-width: 500px) {
    form {
        margin: 20px;
        padding: 20px;
    }

    input[type="submit"] {
        font-size: 14px;
    }
}

</style>
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
        var token = localStorage.getItem("Token");  

        if (!token) {
        	alert("로그인이 필요합니다.");
        	window.close();
        	return;
        }
        
        if (token) {
            fetch('/chargeCash', {
                method: 'GET', 
                headers: {
                    'Authorization': 'Bearer ' + token, 
                    'Content-Type': 'application/json'  
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

        var token = localStorage.getItem("Token");

        var formData = new FormData(document.getElementById("chargeForm"));

        fetch('/submitCharge', {
            method: 'POST',
            body: formData,
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
        .then(response => response.json()) 
        .then(data => {
            alert(data.message || "충전 실패"); 
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
