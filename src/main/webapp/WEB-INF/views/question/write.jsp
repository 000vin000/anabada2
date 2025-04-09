<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고객센터 문의 작성</title>
</head>
<body>
<h2>고객센터 문의 작성</h2>
<form id="questionForm">
    <label for="questionTitle">제목:</label>
    <input type="text" id="questionTitle" name="questionTitle" required /><br>

    <label for="questionContent">내용:</label><br>
    <textarea id="questionContent" name="questionContent" rows="5" cols="40" required></textarea><br>

    <label for="qIsPrivate">비밀글 여부:</label>
    <input type="checkbox" id="qIsPrivate" name="qIsPrivate" value="true" /><br>

    <input type="submit" value="문의하기" />
</form>
<script type="module" src="/js/questionWrite.js"></script>
<p><a href="/mypage">마이페이지로 돌아가기</a></p>
</body>
</html>
