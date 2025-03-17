<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>내가 한 문의 리스트</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"> <%-- 사이드바 css --%>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .body-container {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin: 20px auto;
            max-width: 800px;
        }
        h1 {
            text-align: center;
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
        }
        td.special-column {
		    border: none;
		    pointer-events: none;
		}
        th {
            background-color: #4CAF50;
            color: white;
            text-align: center;
        }
        tr:nth-child(even) td:not(.special-column) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .no-data {
            text-align: center;
            color: #777;
            font-size: 18px;
            margin-top: 20px;
        }
        a {
            color: #4CAF50;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        .black-item-name {
            color: black;
        }

        .toggle-btn {
            cursor: pointer;
            color: black;
            font-weight: bold;
        }

        .toggle-content {
            display: none;
            padding: 10px;
            margin-top: 10px;
            border: 1px solid #ddd;
            background-color: #f9f9f9;
            border-radius: 5px;
        }

        .date {
            float: right;
            font-size: 10px;
            display: inline-block;
            vertical-align: top;
        }

        .toggle-section {
            padding-bottom: 10px;
                       
        }
		.waiting-answer {
            float: right;
            font-size: 10px;
            color: red;
        }
    </style>
</head>
<body>

    <div class="body-container">
        <h1>내가 한 문의 리스트</h1>
        
        <c:if test="${ not empty list }">
            <c:set var="previousItemNo" value="-1" />
            <table>
                <c:forEach var="item" items="${ list }">
                    <c:if test="${item.itemNo != previousItemNo}">
                        <td class="special-column"></td>
                        <tr>
                            <td colspan="2">
                                <a class="black-item-name" href="/item/detail/${item.itemNo}">${item.itemName}</a>
                            </td>
                        </tr>
                    </c:if>

                    <tr>
                        <td colspan="2">
                            <span class="toggle-btn" onclick="toggleVisibility('inquiry-${item.itemNo}-${item.getQNo()}')">
                                ${item.getQTitle()}
                            <c:if test="${ empty item.getAContent() }">
                                <span class="waiting-answer">답변대기</span> 
								<form action="/mypage/q/deleteQ/${item.QNo}/${item.itemNo}" method="post" style="display:inline;">
					                <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?');" style="background-color: gray; color: white; border: none; padding: 5px 10px; cursor: pointer; border-radius: 8px;">
									    삭제
									</button>

					            </form>                                                          
                            </c:if>
                            


                            </span>
                            <div id="inquiry-${item.itemNo}-${item.getQNo()}" class="toggle-content">
                                <div class="toggle-section">
                                    <img src="/images/Q.png" style="width: 50px; height: auto;"><br>
                                    ${ item.getQContent() }
                                    <span class="date">${ item.getFormattedQDate(item.getQDate()) }</span>
                                </div>
                                <div class="toggle-section">
								    <c:if test="${not empty item.getAContent()}">
								        <img src="/images/A.png" style="width: 50px; height: auto;"><br>
								        ${ item.getAContent() }
								        <span class="date">${ item.getFormattedADate(item.getADate()) }</span>
								    </c:if>
								    
								</div>
                            </div>
                        </td>
                    </tr>

                    <c:set var="previousItemNo" value="${item.itemNo}" />
                </c:forEach>
            </table>
        </c:if>

        <c:if test="${ empty list }">
            <div class="no-data">문의내역이 없습니다.</div>
        </c:if>
    </div>
    
    <jsp:include page="../sidebar.jsp" />
    <jsp:include page="../footer.jsp"/>
</body>
<script src="/js/todaypick.js"></script>
<script>
    function toggleVisibility(id) {
        var element = document.getElementById(id);
        if (element.style.display === "none" || element.style.display === "") {
            element.style.display = "block";
        } else {
            element.style.display = "none";
        }
    }
</script>
</html>
