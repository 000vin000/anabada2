<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 수정</title>
</head>
<body>
    <h1>공지사항 수정</h1>
    
    <form action="/notice/update/${notice.noticeNo}" method="post">
        <div>
            <label for="noticeTitle">제목:</label>
            <input type="text" id="noticeTitle" name="noticeTitle" value="${notice.noticeTitle}" required>
        </div>
        
        <div>
            <label for="noticeContent">내용:</label>
            <textarea id="noticeContent" name="noticeContent" rows="10" cols="50" required>${notice.noticeContent}</textarea>
        </div>
        
        <button type="submit">수정하기</button>
        <a href="/management">취소</a> 
    </form>
</body>
</html>
