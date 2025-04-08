<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>코인 전환 신청 수락 페이지</title>
<link rel="stylesheet" href="/css/styleAdmin.css" />
</head>
<body>
	<nav>
        <ul>
        	<li><a href="/">홈</a></li> 
            <li><a href="/admin/dashboard">재무관리</a></li>
            <li><a href="/admin/management">고객관리</a></li>
            <li><a href="/admin/fees">수수료관리</a></li>
            <li><a href="/admin/acceptConversion">코인 전환 신청</a></li>
            <li><a href="/admin/depositWithdrawal">입/출금 관리</a></li>
            <li><a href="/admin/adminRole">관리자 권한 설정</a></li>
        </ul>
    </nav>
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