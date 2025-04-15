<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${date} 수수료 내역</title>
    <link rel="stylesheet" href="/css/dashboard.css">
</head>
<body>
    <h1>${date} 수수료 내역</h1>
    <table border="1">
        <thead>
            <tr>
                <th>수수료 번호</th>
                <th>아이템 번호</th>
                <th>금액</th>
                <th>시간</th>
            </tr>
        </thead>
        <tbody>
    <c:choose>
        <c:when test="${not empty feeDetails}">
            <c:forEach var="fee" items="${feeDetails}">
                <tr>
                    <td>${fee.admincoinNo}</td>
                    <td>${fee.itemNo.itemNo}</td>
                    <td>₩${fee.admincoinAmount}</td>
                    <td>${fee.admincoinAt}</td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="3">해당 날짜에 수수료 내역이 없습니다.</td>
            </tr>
        </c:otherwise>
    </c:choose>
</tbody>
    </table>
    <a href="/admin/fees">뒤로 나가기</a>
</body>
</html>
