<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${month}월 수수료 상세</title>
    <link rel="stylesheet" href="/css/dashboard.css">
</head>
<body>
    <h1>${month}월 수수료 상세 내역</h1>
    <table border="1">
        <thead>
            <tr>
               	<th>번호</th>
               	<th>아이템 번호</th>
                <th>금액</th>
                <th>시간</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="fee" items="${feeDetails}" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td>${fee.itemNo.itemNo}</td>
                <td>₩${fee.admincoinAmount}</td>
                <td>${fee.admincoinAt}</td>
            </tr>  
        </c:forEach> 
        </tbody>
    </table>
    <a href="/admin/fees">뒤로 나가기</a>
</body>
</html>
