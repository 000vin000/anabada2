<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>출금 신청</title>
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