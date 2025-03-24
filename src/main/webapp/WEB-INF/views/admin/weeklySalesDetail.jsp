<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주매출 상세보기</title>
</head>
<body>

<h1>주매출 상세보기</h1>

<p>주 매출 기간: ${startOfWeek} ~ ${endOfWeek}</p>

<!-- 주 매출 테이블 -->
<table border="1">
    <thead>
        <tr>
            <th>결제 번호</th>
            <th>결제 금액</th>
            <th>결제 완료 일자</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="sale" items="${weeklySales}">
            <tr>
                <td>${sale[0]}</td>  <!-- 결제 번호 출력 -->
                <td>${sale[1]}</td>  <!-- 결제 금액 출력 -->
                <td>${sale[2]}</td>  <!-- 결제 완료 일자 출력 -->
            </tr>
        </c:forEach>
    </tbody>
</table>

<a href="/dashboard">대시보드로 돌아가기</a>

</body>
</html>
