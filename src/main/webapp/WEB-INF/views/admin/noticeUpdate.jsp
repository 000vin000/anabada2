<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>공지사항 수정</title>
</head>
<body>
    <div class="container">
        <h2>공지사항 수정</h2>

        <!-- 수정 폼 -->
        <form id="editNoticeForm" method="post">
            <div class="form-group">
                <label for="noticeTitle">제목</label>
                <input type="text" id="noticeTitle" name="noticeTitle" value="${notice.noticeTitle}" required>
            </div>

            <div class="form-group">
                <label for="noticeContent">내용</label>
                <textarea id="noticeContent" name="noticeContent" required>${notice.noticeContent}</textarea>
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary">수정하기</button>
                <a href="${pageContext.request.contextPath}/admin/management" class="btn btn-secondary">취소</a>
            </div>
            <input type="hidden" id="noticeNo" name="noticeNo" value="${notice.noticeNo}" />
        </form>
    </div>
    <script type="module" src="/js/admin/noticeUpdate.js"></script>
</body>
</html>
