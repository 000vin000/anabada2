<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>출금 신청</title>
<style>
    html, body {
        margin: 0;
        padding: 0;
        height: 550px;
        overflow: hidden;
        font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
        background-color: #f1f3f5;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    form#withdrawal {
        background-color: #ffffff;
        padding: 24px;
        border-radius: 14px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
        width: 360px;
        max-height: 510px;
        box-sizing: border-box;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    #currentCash {
        font-size: 18px;
        font-weight: 600;
        text-align: center;
        margin-bottom: 10px;
        color: #333;
    }

    form p {
        margin: 8px 0 4px;
        font-size: 15px;
        color: #333;
    }

    input[type="number"],
    input[type="text"],
    select {
        width: 100%;
        padding: 8px 10px;
        font-size: 15px;
        border: 1px solid #ccc;
        border-radius: 8px;
        box-sizing: border-box;
        margin-bottom: 10px;
    }

    div[style*="background-color: #C8FAC8"] {
        background-color: #f0fff0;
        padding: 14px;
        border-radius: 10px;
        margin: 10px 0;
    }

    input[type="submit"] {
        width: 100%;
        background-color: #00b894;
        color: white;
        padding: 10px;
        font-size: 15px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-weight: 600;
        transition: background-color 0.3s ease;
        margin-top: 8px;
    }

    input[type="submit"]:hover {
        background-color: #019875;
    }

    .note {
        font-size: 13px;
        color: #828282;
        margin: 0 0 10px;
    }
</style>

</head>
<body>
	<form id="withdrawal" method="post" action="/withdrawalApplication" onsubmit="return withdrawal(event)">
		<!-- 보유중인 금액 표시 -->
		<div id="currentCash"></div>
		
		<!-- 출금할 금액 -->
		<div>
			<p>출금할 금액
				<input type="number" id="withdrawalCash" name="withdrawalCash" min="1000" step="1" required>
			</p>
			<p style="color: #828282">* 출금 신청 최소 금액은 1,000원 입니다.</p>
		</div>
		
		<!-- 은행 정보 -->
		<div style="background-color: #C8FAC8">
		<p>
			<label for="withdrawalBank">은행</label>
			<select id="withdrawalBank" name="withdrawalBank">
			    <option value="KB" selected>국민은행</option>
			    <option value="Shinhan">신한은행</option>
			    <option value="Woori">우리은행</option>
			    <option value="Hana">하나은행</option>
			    <option value="DGB">대구은행</option>
			    <option value="BNK-K">경남은행</option>
			    <option value="Kwangju">광주은행</option>
			    <option value="Jeju">제주은행</option>
			    <option value="BNK-B">부산은행</option>
			    <option value="JB">전북은행</option>
			    <option value="Citibank">씨티은행</option>
			    <option value="SC-First">제일은행</option>
			    <option value="Kakao">카카오뱅크</option>
			    <option value="Toss">토스뱅크</option>
			    <option value="K">케이뱅크</option>
			    <option value="IBK">기업은행</option>
			    <option value="NH">농협은행</option>
			    <option value="KDB">산업은행</option>
			</select>
		</p>
		<p>
			<label for="withdrawalAccount">계좌 번호</label>
			<input type="text" id="withdrawalAccount" name="withdrawalAccount" placeholder="'-' 없이 입력" required>
		</p>
		</div>
		
		<input type="submit" value="출금 신청">
	</form>
</body>
<script src="../js/getCurrentCash.js"></script>
<script src="/js/withdrawalCash.js"></script>
<script>
	window.onload = function() {
		var token = localStorage.getItem("Token");  

        if (!token) {
        	alert("로그인이 필요합니다.");
        	window.close();
        	return;
        }
        
        loadCurrentCash();
	}
</script>
</html>