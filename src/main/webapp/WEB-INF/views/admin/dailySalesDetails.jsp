<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>일매출 상세보기</title>
</head>
<body>

<h1>일매출 상세보기</h1>

<p>일매출 기간: ${startPeriod} ~ ${endPeriod}</p>

<!-- 일매출 테이블 -->
<table border="1">
    <thead>
        <tr>
            <th>결제 번호</th>
            <th>금액</th>
            <th>결제일</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="payment" items="${dailyPayments}">
            <tr>
                <td>${payment.payNo}</td>
                <td>₩${payment.payPrice}</td>
                <td>${payment.payCompletedDate}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<a href="/admin/dashboard">대시보드로 돌아가기</a>

</body>
</html>
