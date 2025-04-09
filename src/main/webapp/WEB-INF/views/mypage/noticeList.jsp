<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 목록</title>
    <style>
        table {
            width: 70%;
            border-collapse: collapse;
            margin-top: 20px;
            margin: 20px auto;
        }

        th, td {
            border: 1px solid #aaa;
            padding: 10px;
            text-align: center;
        }

        th {
            background-color: #f2f2f2;
        }

        h2 {
            text-align: center;
            margin-top: 40px;
        }
    </style>
</head>
<body>
    <h2>공지사항 목록</h2>
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
            <c:forEach var="notice" items="${noticeList}">
                <tr>
                    <td>${notice.noticeNo}</td>
                    <td>${notice.noticeTitle}</td>
                    <td>${notice.noticeContent}</td>
                    <td>${notice.noticeCreatedDate}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="/mypage">마이페이지로 돌아가기</a></p>
</body>
</html>
