<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내가 받은 경고</title>
<style>
	h2 {
		margin-top: 30px;
		color: #333;
		text-align: center;
	}

	p {
		text-align: center;
		color: #555;
		margin-bottom: 20px;
	}

	.warnTable {
		display: flex;
		justify-content: center;
		margin-top: 20px;
		width: 100%;
	}

	table {
		border-collapse: collapse;
		width: 90%;
		max-width: 800px;
		background-color: white;
		border-radius: 10px;
		overflow: hidden;
		border: none;
	}

	th, td {
		padding: 12px 15px;
		text-align: center;
	}

	th {
		background-color: #fde4e4;
		color: black;
		font-weight: bold;
	}

	tr:hover {
		background-color: #f9f9f9;
	}

	.warnError {
		color: red;
		text-align: center;
		margin-top: 20px;
		font-size: 16px;
	}

	button {
		position: fixed;
		bottom: 30px;
		left: 50%;
		transform: translateX(-50%);
		display: block;
		margin: 30px auto 0;
		padding: 10px 20px;
		font-size: 16px;
		color: #c53030;
		background-color: #fde4e4;
		border: none;
		border-radius: 5px;
		font-weight: 700;
		cursor: pointer;
		transition: transform 0.2s;
	}

	button:hover {
		transform: translateX(-50%) scale(1.05);
		transition: transform 0.2s;
	}
</style>
</head>
<body>
	<h2>🚨 받은 경고 🚨</h2>
	<p>* 누적 5회 => 영구 정지 및 회원 탈퇴 *</p>
	<div class="warnTable">
		<table>
			<thead>
				<tr>
					<th>번호</th>
					<th>받은 날짜</th>
					<th>신고 사유</th>
					<th>관련 링크</th>
					<th>처리 결과</th>
				</tr>
			</thead>
			<tbody>
				<!-- 받은 경고 내역 -->
			</tbody>
		</table>
		<div class="warnError"></div>
	</div>
	<div>
		<button onclick="closeWarnList()">닫기</button>
	</div>
</body>
<script src="/js/myWarnList.js"></script>
<script>
	function closeWarnList() {
		window.close();
	}
</script>
</html>
