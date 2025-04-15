<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>공지사항 상세보기</title>
    <link rel="stylesheet" href="/css/notice.css" />
</head>
<body>

<h2>${notice.noticeTitle}</h2>

<p>
    작성자: ${notice.admin.userId}<br>
    작성일: ${notice.noticeCreatedDate} <br>
    수정일: ${notice.noticeUpdatedDate}
</p>

<hr>

<p>
    ${notice.noticeContent}
</p>


<br>
<a href="/notice/list">목록으로</a>

</body>
</html>
