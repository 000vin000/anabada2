<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입찰 기록</title>
<style>
	h2 {
		margin-top: 30px;
		color: #333;
		text-align: center;
	}

	.bidTable {
		display: flex;
		justify-content: center;
		margin-top: 20px;
	}

	table {
		border-collapse: collapse;
		width: 90%;
		max-width: 700px;
		background-color: white;
		border-radius: 10px;
		overflow: hidden;
		border: white; 
	}

	th, td {
		padding: 12px 15px;
		text-align: center;
	}

	th {
		background-color: #dbf5f9;
		color: black;
		font-weight: bold;
	}

	tr:hover {
		background-color: #f1f1f1;
	}

	.bidListError {
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
		color: #007d8a; 
		background-color: #dbf5f9; 
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
	<h2>입찰 기록</h2>
	<div class="bidTable">
		<c:if test="${not empty bidList}">
			<table border="1">
				<thead>
					<tr>
						<th>번호</th>
			            <th>입찰 시간</th>
			            <th>입찰자</th>
			            <th>가격</th>
				    </tr>
				</thead>
				<tbody>
					<tr>
						<c:forEach var="bid" items="${bidList}">
							<td>${status.index + 1}</td>
				            <td>${bid.formatToKoreanDate(bid.bidTime)}</td>
				            <td>${bid.user.userNick}</td>
				            <td>${bid.formatBigDecimal(bid.bidPrice)}</td>
				        </c:forEach>
					</tr>
				</tbody>
			</table>
		</c:if>
		<c:if test="${empty bidList}">
			<div class="bidListError">${error}</div>
		</c:if>
	</div>
	<div>
		<button onclick="closeBidList()">닫기</button>
	</div>
</body>
<script>
	function closeBidList() {
		window.close();
	}
</script>
</html>