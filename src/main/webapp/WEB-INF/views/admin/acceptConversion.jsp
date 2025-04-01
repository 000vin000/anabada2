<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>코인 전환 신청 수락 페이지</title>
</head>
<body>
	<!-- 현금 -> 코인 전환 신청 내역 -->
	<div id="acceptConversionToCoin">
		<h2>코인 전환 신청 내역</h2>
		<button>전체 수락</button>
		<table border="1">
			<thead>
				<tr>
					<th>번호</th>
					<th>신청 일자</th>
					<th>신청인</th>
					<th>신청 금액</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<!-- 내역 -->
			</tbody>
		</table>
	</div>
	
	<hr>
	
	<!-- 코인 -> 현금 전환 신청 내역 -->
	<div id="acceptConversionToCash">
		<h2>현금 전환 신청 내역</h2>
		<button>전체 수락</button>
		<table border="1">
			<thead>
				<tr>
					<th>번호</th>
					<th>신청 일자</th>
					<th>신청인</th>
					<th>신청 금액</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<!-- 내역 -->
			</tbody>
		</table>
	</div>
</body>
<script src="/js/admin/acceptConversion.js"></script>
</html>