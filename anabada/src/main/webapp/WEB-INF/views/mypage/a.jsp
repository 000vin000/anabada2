<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>내가 받은 문의 리스트</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
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
        tr:nth-child(even) td:not(.special-column){
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

        .black-item-name {
            color: black;
        }

        .answer-form {
            display: none;
            padding: 10px;
            background-color: #f9f9f9;
            border-radius: 5px;
            border: 1px solid #ddd;
            margin-top: 10px;
        }

        .waiting-answer {
            float: right;
            font-size: 10px;
            color: red;
        }
    </style>
    <script>
 	// 현재 열린 토글과 버튼을 추적할 변수
    let openToggle = null;
    let openButton = null;

    // 토글 함수 수정
    function toggleEditForm(qNo, button) {
        var form = document.getElementById('editForm-' + qNo);

        // 기존에 열려있는 토글이 있으면 닫고 버튼 색상 원래대로 복구
        if (openToggle && openToggle !== form) {
            openToggle.style.display = "none";
            if (openButton) openButton.style.backgroundColor = "#21afbf"; // 이전 버튼 색상 원래대로
        }

        // 새로운 폼 토글
        if (form.style.display === "none" || form.style.display === "") {
            form.style.display = "table-row"; // 폼 보이기
            button.style.backgroundColor = "#00d4da"; // 버튼 색상 변경
            openToggle = form;
            openButton = button; // 현재 버튼 저장
        } else {
            form.style.display = "none"; // 폼 숨기기
            button.style.backgroundColor = "#21afbf"; // 원래 색상으로 변경
            openToggle = null;
            openButton = null; // 버튼도 초기화
        }
    }
    
    // 상품별 문의 목록을 토글하는 함수
    function toggleItem(button) {
        var myQuestions = document.getElementById('item');
        
        // 이미 열려있는 토글을 닫기
        if (openToggle && openToggle !== item) {
            openToggle.style.display = "none";
            openToggle.previousElementSibling.style.backgroundColor = "#21afbf"; // 이전 버튼 색상 원래대로
        }

        // 새로운 폼 토글
        if (item.style.display === "none" || item.style.display === "") {
        	item.style.display = "block"; // 나의 문의 보이기
            button.style.backgroundColor = "#00d4da"; // 버튼 색상 변경
            openToggle = item;
        } else {
        	item.style.display = "none"; // 나의 문의 숨기기
            button.style.backgroundColor = "#21afbf"; // 원래 색상으로 변경
            openToggle = null;
        }
    }
    
    </script>
</head>
<body>

    <div class="body-container">
        <h1>내가 받은 문의 리스트</h1>
        
        <c:if test="${ not empty list }">
            <c:set var="previousItemNo" value="-1" />
            <table>
                <c:forEach var="item" items="${ list }">
                    <!-- Display the product name only once -->
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
                            <span class="toggle-btn" onclick="toggleVisibility('inquiry-${item.itemNo}-${item.getQNo()}')">${item.getQTitle()}</span>
                            
                            <c:if test="${ empty item.getAContent() }">
                                <span class="waiting-answer">답변대기</span>
                            </c:if>
                            <div id="inquiry-${item.itemNo}-${item.getQNo()}" class="toggle-content">
                                <!-- Question Content -->
                                <div class="toggle-section">
                                    <img src="/images/Q.png" style="width: 50px; height: auto;"><br>
                                    ${ item.getQContent() }
                                    <span class="date">${ item.getFormattedQDate(item.getQDate()) }</span>
                                </div>

                                <c:if test="${ not empty item.getAContent() }">
                                    <div class="toggle-section">
                                        <img src="/images/A.png" style="width: 50px; height: auto;"><br>
                                        ${ item.getAContent() }
                                        <span class="date">${ item.getFormattedADate(item.getADate()) }</span>
                                    </div>
                                </c:if>

                          
                                <c:if test="${ empty item.getAContent() }">
                                    <div class="answer-form" id="answerForm-${item.getQNo()}">
                                        <form action="/mypage/a/insertA/${item.getQNo()}" method="post">
                                            <input type="hidden" name="qNo" value="${item.getQNo()}">
                                            <label for="aContent"></label>
                                            <textarea name="aContent" required></textarea><br><br>
                                            <input type="image" src="/images/A.png" style="width: 50px; height: auto;" alt="답변 등록" />
                                        </form>
                                    </div>
                                </c:if>

                            </div>
                        </td>
                    </tr>

                    <!-- Remove the border line between inquiries -->
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
	window.onload = function() {
	   
	    var urlParams = new URLSearchParams(window.location.search);
	    var qNo = urlParams.get('qNo');  
	    
	   
	    if (qNo) {
	       
	        var element = document.getElementById('inquiry-' + qNo);  
	        var answerForm = document.getElementById('answerForm-' + qNo);  
	
	        if (element && answerForm) {
	            element.style.display = "block";  
	            answerForm.style.display = "block";  
	        }
	    }
	}

   
    function toggleVisibility(id) {
        var element = document.getElementById(id);
        var answerForm = document.getElementById('answerForm-' + id.split('-')[2]);

        if (element.style.display === "none" || element.style.display === "") {
            element.style.display = "block";
            if (answerForm) {
                answerForm.style.display = "block";
            }
        } else {
            element.style.display = "none";
            if (answerForm) {
                answerForm.style.display = "none"; 
            }
        }
    }
</script>

</html>
