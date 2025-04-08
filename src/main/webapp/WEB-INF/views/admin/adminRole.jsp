<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 권한 설정</title>
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

    <div class="container">
        <h2>관리자 권한 설정</h2>

        <div class="search-section">
            <input type="text" id="searchUserId" placeholder="사용자 아이디 입력" />
            <button id="searchBtn">검색</button>
        </div>

        <div id="result" class="mt-3"></div>
    </div>
   	 <script type="module" src="/js/admin/adminRole.js"></script>
</body>
</html>
