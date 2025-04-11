<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<body>
	<!-- 사용자 정보 -->
	<div class="user-container">
		<div class="user-header">
			<div id="user-name"><!-- 사용자 닉네임(아이디) --></div>
			<a id="updateInfoBtn" href="#">회원정보 수정</a>
		</div>
		<div id="user-info"><!-- 사용자 정보 : 이름, 전화번호, 가입 일자  --></div>
	</div>

	<!-- 보유 현금/코인 -->
	<div class="coin-container">
		<div class="cashSection section">
	    	<div id="currentCash" class="section-title">
	    		<img src="/images/myPage/money.png"> 
	    		<!-- 보유 현금 -->
			</div>
			<div class="button-group btnForCash">
			  	<button class="openWindow" onclick="openNewWindow('금액 충전', '/chargeCash')">금액 충전</button>
				<button class="openWindow" onclick="openNewWindow('코인으로 전환', '/toCoin')">코인으로 전환</button>
				<button class="openWindow" onclick="openNewWindow('출금 신청', '/withdrawal')">출금 신청</button>
			    <button class="openList" id="chargeList">충전 내역</button>
			    <button class="openList" id="toCoinList">전환 신청 내역</button>
			    <button class="openList" id="withdrawalList">출금 신청 내역</button>
	  		</div>
		</div>

  		<div class="coinSection section">
    		<div id="currentCoin" class="section-title"> 
		    	<img src="/images/myPage/coin.png"> 
		    	<!-- 보유 코인 -->
    		</div>
    		<div class="button-group btnForCoin">
				<button class="openWindow" onclick="openNewWindow('현금으로 전환', '/toCash')">현금으로 전환</button>
				<button class="openList" id="toCashList">신청 내역</button>
				<button class="openList" id="useCoin">코인 변동 내역</button>
    		</div>
  		</div>
	</div>
    
    <!-- 충전 내역 모달 -->
    <div id="chargeListModal" class="modal">
    	<div class="modal-content">
    		<span class="close" onclick="closeChargeListModal()">&times;</span>
    		<h2>충전 내역</h2>
    		<table border="1">
				<thead>
					<tr>
						<th>충전 일자</th>
						<th>금액</th>
						<th>충전 방식</th>
						<th>상태</th>
					</tr>
				</thead>
				<tbody>
					<!-- 내역 -->
				</tbody>
			</table>
    	</div>
    </div>
    
	<!-- 전환 신청 내역 모달 -->
	<div id="conversionModal" class="modal">
		<div class="modal-content">
			<span class="close" onclick="closeConversionModal()">&times;</span>
			<h2>신청 내역</h2>
			<table border="1">
				<thead>
					<tr>
						<th>신청 일자</th>
						<th>금액</th>
						<th>상태</th>
						<th>전환 일자</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<!-- 내역 -->
				</tbody>
			</table>
		</div>
	</div>
	
	<!-- 현금 출금 내역 모달 -->
	<div id="withdrawalModal" class="modal">
		<div class="modal-content">
			<span class="close" onclick="closeWithdrawalModal()">&times;</span>
			<h2>출금 신청 내역</h2>
			<table border="1">
				<thead>
					<tr>
						<th>신청 일자</th>
						<th>금액</th>
						<th>상태</th>
						<th>계좌 정보</th>
						<th>출금 일자</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<!-- 내역 -->
				</tbody>
			</table>
		</div>
	</div>
	
	<!-- 코인 변동 내역 모달 -->
	<div id="useCoinModal" class=modal>
		<div class="modal-content">
			<span class="close" onclick="closeUseCoinModal()">&times;</span>
			<h2 style="margin-top: 5px; margin-bottom: 5px;">코인 변동 내역</h2>
			<p style="margin-top: 5px; margin-bottom: 5px;">
				<label style="color: blue">획득</label> / <label style="color: red">사용</label>
			</p>
			<table border="1" style="margin-top: 5px;">
				<thead>
					<tr>
						<th>일자</th>
						<th>내용</th>
						<th>금액</th> <!-- 획득(charge, cancel, winning)/사용(bid, fee, cash) -->
						<th>상품</th>
					</tr>
				</thead>
				<tbody>
					<!-- 내역 -->
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="module">
import {goToUpdateInfo} from '/js/user/goToUpdateInfo.js';
window.goToUpdateInfo = goToUpdateInfo;
</script>
<script>
function openNewWindow(name, url) {
	window.open(url, name, "width=500,height=300,top=200,left=600,resizable=yes,scrollbars=yes,status=no,menubar=no,toolbar=no,location=no");
}
</script>
<script src="/js/mypage.js"></script>
<script src="/js/chargeListModal.js"></script>
<script src="/js/conversionModal.js"></script>
<script src="/js/useCoinModal.js"></script>
<script src="/js/conversionCoinModal.js"></script>
<script src="/js/withdrawalModal.js"></script>
