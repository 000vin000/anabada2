<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고객센터 문의 작성</title>
<link rel="stylesheet" type="text/css" href="/css/question.css" />
</head>
<body>
<div class="container">
    <h2>고객센터 문의 작성</h2>
    <form id="questionForm">
        <label for="questionTitle">제목</label>
        <input type="text" id="questionTitle" name="questionTitle" required />

        <label for="questionContent">내용</label>
        <textarea id="questionContent" name="questionContent" rows="6" required></textarea>

        <label>
            <input type="checkbox" id="qIsPrivate" name="qIsPrivate" value="true" />
            비밀글로 작성하기
        </label>

        <input type="submit" value="문의하기" />
    </form>
    <a class="back-link" href="/mypage">← 마이페이지로 돌아가기</a>
</div>

<script type="module" src="/js/questionWrite.js"></script>
</body>
</html>
