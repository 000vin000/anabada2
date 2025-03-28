<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<body>
	<div class="cashSection" style="background-color: #FFF0F0">
		<div id="currentCash"><!-- 보유금액 --></div>
		<button onclick="openChargeCash('금액충전', '/chargeCash')">금액충전</button>
		<p>코인으로 전환</p>
	</div>
	<div class="coinSection">
		<div id="currentCoin"><!-- 보유코인 --></div>
		<p>거래 내역</p>
	</div>

</body>
<script>
	function openChargeCash(name, url) {
    	window.open(url, name, "width=500,height=300,top=200,left=600,resizable=yes,scrollbars=yes,status=no,menubar=no,toolbar=no,location=no");
	}
</script>
<script src="/js/mypage.js"></script>
