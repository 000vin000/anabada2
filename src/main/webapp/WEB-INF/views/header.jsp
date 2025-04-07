<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="/images/favicon.png" sizes="16x16">
    <title>ANABADA</title>
    <script defer src="/js/header.js"></script>
</head>

<body>
    <header>
        <a href="/"><img src="/images/logo.png" alt="로고" /></a>

        <div id="findOption">
            <select id="findType" name="findType">
                <option value="" selected disabled>검색유형</option>
                <option value="itemName">상품명</option>
                <option value="userNick">닉네임(동일)</option> 
            </select>
            <input type="search" id="keyword" name="keyword" placeholder="검색어를 입력하세요" onkeydown="handleKeyPress(event)">
            <input type="button" id="find" value="검색" onclick="submitSearch()">
        </div>

        <nav>
            <ul>
                <li><a id="loginBtn" href="/auth/login/individual/IndividualLogin.html">로그인</a></li>
                <li><a id="joinBtn" href="/auth/join/individual/IndividualJoin.html">회원가입</a></li>
                <li><a id="mypageBtn" href="/mypage">마이페이지</a></li>
                <li><a id="logoutBtn" href="#" onclick="logout()">로그아웃</a></li>
                <li><a id="itemUpload" href="/item/mypage/itemup" style="display: none;">상품 등록</a></li>
            </ul>
        </nav>
    </header>

    <script type="module" src="/js/user/common/authCheck.js"></script>
    <script type="module" src="/js/user/common/logout.js"></script>
</body>
</html>
