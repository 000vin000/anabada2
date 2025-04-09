<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<body>
<div class="coin-container">
  <div class="cashSection section">
    <div id="currentCash" class="section-title">
    	<img src="/images/myPage/money.png"> 
    	<!-- 보유금액 -->
    </div>
    <div class="button-group btnForCash">
      <button class="openWindow" onclick="openChargeCash('금액 충전', '/chargeCash')">금액충전</button>
      <button class="openWindow" onclick="openCashToCoin('코인으로 전환', '/toCoin')">코인으로 전환</button>
	  <button class="openWindow" onclick="openWithdrawal('출금 신청', '/withdrawal')">출금 신청</button>
      <button class="openList" id="chargeList">충전 내역</button>
      <button class="openList" id="toCoinList">전환 신청 내역</button>
      <button class="openList" id="withdrawalList">출금 신청 내역</button>
    </div>
  </div>

  <div class="coinSection section">
    <div id="currentCoin" class="section-title"> 
    	<img src="/images/myPage/coin.png"> 
    	<!-- 보유코인 -->
    </div>
    <div class="button-group btnForCoin">
      <button class="openWindow" onclick="openCoinToCash('현금으로 전환', '/toCash')">현금으로 전환</button>
      <button class="openList" id="toCashList">신청 내역</button>
	  <button class="openList" id="useCoin">코인 변동 내역</button>
    </div>
  </div>
</div>

    <!-- 모달 스타일 -->
    <style>
        /* 모달 배경 */
        .modal {
            display: none; /* 기본적으로 보이지 않도록 설정 */
            position: fixed;
            z-index: 1; /* 화면 위에 표시되도록 설정 */
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.4); /* 반투명 검은 배경 */
        }

        /* 모달 콘텐츠 */
        .modal-content {
            background-color: #fff;
            margin: 5% auto; /* 화면의 중간에 위치하도록 설정 */
            padding: 20px;
            border: 1px solid #888;
            width: 80%; /* 화면 너비의 80% */
            max-width: 500px; /* 최대 너비 설정 */
            border-radius: 10px; /* 모서리 둥글게 */
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2); /* 그림자 추가 */
        }

        /* 모달 닫기 버튼 */
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }

        /* 테이블 */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        td {
            text-align: center;
            color: #00000;
        }
    </style>
    
    <!-- 충전 내역 모달 -->
    <div id="chargeListModal" class="modal" style="display: none;">
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
	<div id="conversionModal" class="modal" style="display: none;">
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
	<div id="withdrawalModal" class="modal" style="display: none;">
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
	<div id="useCoinModal" class=modal style="display: none;">
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
<script>
function openChargeCash(name, url) {
	window.open(url, name, "width=500,height=300,top=200,left=600,resizable=yes,scrollbars=yes,status=no,menubar=no,toolbar=no,location=no");
}
function openCashToCoin(name, url) {
	window.open(url, name, "width=500,height=300,top=200,left=600,resizable=yes,scrollbars=yes,status=no,menubar=no,toolbar=no,location=no");
}
function openCoinToCash(name, url) {
	window.open(url, name, "width=500,height=300,top=200,left=600,resizable=yes,scrollbars=yes,status=no,menubar=no,toolbar=no,location=no");
}
function openWithdrawal(name, url) {
	window.open(url, name, "width=500,height=300,top=200,left=600,resizable=yes,scrollbars=yes,status=no,menubar=no,toolbar=no,location=no");
}
</script>
<script src="/js/mypage.js"></script>
<script src="/js/chargeListModal.js"></script>
<script src="/js/conversionModal.js"></script>
<script src="/js/useCoinModal.js"></script>
<script src="/js/conversionCoinModal.js"></script>
<script src="/js/withdrawalModal.js"></script>
