<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <title>공지사항 등록</title>
</head>
<body>

    <h1>공지사항 등록</h1>

    <form action="/admin/notice/save" method="post">
        <div>
            <label for="noticeTitle">제목</label>
            <input type="text" id="noticeTitle" name="noticeTitle" placeholder="공지사항 제목을 입력하세요" required>
        </div>
        
        <div>
            <label for="noticeContent">내용</label>
            <textarea id="noticeContent" name="noticeContent" rows="6" placeholder="공지사항 내용을 입력하세요" required></textarea>
        </div>

        <div>
            <button type="submit">등록</button>
            <a href="/admin/management"><button type="button">취소</button></a>
        </div>
    </form>

</body>
</html>
