<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>월매출 상세보기</title>
</head>
<body>

<h1>월매출 상세보기</h1>

<p>월 매출 기간: ${startOfMonth} ~ ${endOfMonth}</p>

<!-- 월 매출 테이블 -->
<table border="1">
    <thead>
        <tr>
            <th>월</th>
            <th>총 매출</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="sale" items="${monthlySales}">
            <tr>
                <td>${sale[0]}</td>  <!-- 월 -->
                <td>${sale[1]}</td>  <!-- 총 매출 -->
            </tr>
        </c:forEach>
    </tbody>
</table>

<a href="/dashboard">대시보드로 돌아가기</a>

</body>
</html>

