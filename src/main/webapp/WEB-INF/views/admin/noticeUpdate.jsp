<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 수정</title>
    <link rel="stylesheet" href="/css/notice.css" />
</head>
<body>
    
        <h1>공지사항 수정</h1>

        <!-- 수정 폼 -->
        <form id="editNoticeForm" method="post">
                <label for="noticeTitle">제목</label>
                <input type="text" id="noticeTitle" name="noticeTitle" value="${notice.noticeTitle}" required>
                
                <label for="noticeContent">내용</label>
                <textarea id="noticeContent" name="noticeContent" required>${notice.noticeContent}</textarea>
                
                <button type="submit" class="btn btn-primary">수정</button>
                <a href="${pageContext.request.contextPath}/admin/management"><button type="button" class="btn btn-primary">취소</button></a>
            <input type="hidden" id="noticeNo" name="noticeNo" value="${notice.noticeNo}" />
        </form>

    <script type="module" src="/js/admin/noticeUpdate.js"></script>
</body>
</html>
