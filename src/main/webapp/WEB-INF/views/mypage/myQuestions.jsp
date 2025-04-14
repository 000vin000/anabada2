<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 문의사항 목록</title>
    <link rel="stylesheet" type="text/css" href="/css/question.css" />
</head>
<body>

<h2>내 문의사항 목록</h2>

<table border="1">
    <thead>
        <tr>
            <th>번호</th>
            <th>문의 제목</th>
            <th>문의 내용</th>
            <th>답변 내용</th>
            <th>수정</th>
            <th>삭제</th>
        </tr>
    </thead>
    <tbody id="questionsTable">
    </tbody>
</table>
<script type="module" src="/js/myQuestions.js"></script>
<p><a href="/mypage">← 마이페이지로 돌아가기</a></p>
</body>
</html>