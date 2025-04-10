<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 등록</title>
</head>
<body>

    <h1>공지사항 등록</h1>

    <form id="noticeForm">
        <label for="noticeTitle">제목:</label>
        <input type="text" id="noticeTitle" name="noticeTitle" required><br>

        <label for="noticeContent">내용:</label><br>
        <textarea id="noticeContent" name="noticeContent" rows="5" cols="40" required></textarea><br>

        <input type="submit" value="등록">
        <a href="/admin/management"><button type="button">취소</button></a>
    </form>
    <script type="module" src="/js/admin/noticeCreate.js"></script>
</body>
</html>
