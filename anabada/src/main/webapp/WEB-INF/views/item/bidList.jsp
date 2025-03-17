<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>입찰기록</title>
    <style>
        body {
            margin: 30px;
        }
        h1 {
            color: #333;
            margin: 0 auto;
            text-align: center; /* 텍스트 가운데 정렬 추가 */
            margin-bottom: 20px;
        }
        .container {
            padding: 10px;
            border-radius: 8px;
        }
        table {
            width: 90%;
            border-collapse: collapse;
            margin: 0 auto;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #e7f9fa;
            text-align: center; /* 자식 요소의 텍스트 가운데 정렬 */
        }
        th {
            background-color: #21afbf; 
            color: white;
            font-size: 17px;
        }
        .no-data {
            font-size: 1.2em;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<h1>입찰기록</h1>
    <div class="container">
        <c:if test="${ not empty list }">
            <table>
               <thead>
                <tr>                 
                    <th scope="col">입찰시간</th>
                    <th scope="col">입찰가</th>
                    <th scope="col">입찰자</th>             
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${ list }">
                    <tr>                        
                        <td>${ item.getTimeStr(item.bidTime) }</td>
                        <td>${ item.addCommas(item.getBidPrice()) }</td>
                        <td>${ item.getUserNick() }</td>
                    </tr>
                </c:forEach>
            </tbody>
            </table>
        </c:if> 
        <c:if test="${ empty list }">
            <div class="no-data">입찰내역이 없습니다.</div>
        </c:if>
    </div>
</body>
</html>
