<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입/출금 확인 페이지</title>
</head>
<body>
	<!-- 입금 내역 -->
	<div id="forDepositList">
		<h2>입금 내역</h2>
		<table border="1">
			<thead>
				<tr>
					<th>번호</th>
					<th>입금 일자</th>
					<th>신청인</th>
					<th>입금 금액</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<!-- 내역 -->
			</tbody>
		</table>
	</div>
	
	<hr>
	
	<!-- 출금 신청 내역 -->
	<div id="forWithdrawalList">
		<h2>출금 신청 내역</h2>
		<table border="1">
			<thead>
				<tr>
					<th>번호</th>
					<th>신청 일자</th>
					<th>신청인</th>
					<th>출금 금액</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<!-- 내역 -->
			</tbody>
		</table>
	</div>
</body>
<script src="/js/admin/depositWithdrawal.js"></script>
</html>