<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 목록</title>
    <link rel="stylesheet" href="/css/notice.css" />
</head>
<body>
    <h1>공지사항 목록</h1>
    <table>
        <thead>
            <tr>
                <th>공지번호</th>
                <th>공지제목</th>
                <th>공지내용</th>
                <th>작성일</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="notice" items="${noticeList}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>
    				<a href="/notice/detail?noticeNo=${notice.noticeNo}">
       					 ${notice.noticeTitle}
   					</a>
					</td>
                    <td>${notice.noticeContent}</td>
                    <td>${notice.noticeCreatedDate}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a class="back-link" href="/mypage">← 마이페이지로 돌아가기</a>
</body>
</html>
