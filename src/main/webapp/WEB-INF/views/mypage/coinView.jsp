<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<body>
	<div class="cashSection" style="background-color: #FFF0F0">
		<p>보유금액</p>
		<button onclick="openChargeCash('금액충전', '/chargeCash')">금액충전</button>
		<p>코인으로 전환</p>
	</div>
	<div class="coinSection">
		<p>코인 잔액</p>
		<p>거래 내역</p>
	</div>

</body>
<script>
	function openChargeCash(name, url) {
    	window.open(url, name, "width=500,height=300,top=200,left=600,resizable=yes,scrollbars=yes,status=no,menubar=no,toolbar=no,location=no");
	}
</script>
