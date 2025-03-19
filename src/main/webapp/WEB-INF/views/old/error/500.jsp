<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>500 error</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<style>
    #error {
       width: 1000px;
       margin: 0 auto;
       margin-top: 20px;
        
        padding: 20px; /* 내부 여백 */
        border-radius: 5px; /* 모서리 둥글게 */
        text-align: center; /* 텍스트 가운데 정렬 */
        background-color: #e7f9fa;
    }

    #toMain {
        display: block; /* 버튼 모양 만들기 */
        width: 200px;
        margin: 20px auto; /* 위쪽 여백 */
        padding: 10px 20px; /* 버튼 내부 여백 */
        background-color: #21afbf; /* 배경색 */
        color: white; /* 글자색 */
        border: none; /* 테두리 없애기 */
        border-radius: 5px; /* 모서리 둥글게 */
        text-decoration: none; /* 밑줄 없애기 */
        font-size: 16px; /* 글자 크기 */
        cursor: pointer; /* 마우스 커서 변경 */
        text-align: center;
    }
    
    #errorMain {
       font-size: 20px;
      font-weight: 700;
    }
</style>
</head>
<body>
	<div class="body-container">
	    <div id="error">
	        <p id="errorMain">요청하신 페이지를 표시할 수 없습니다.</p>
	        <p>잘못 검색하셨거나 종료된 화면, 또는 해당 화면에서 에러가 발생해서</p>
	        <p>표시할 수가 없습니다.</p>
	    </div>
	    <a href="/" id="toMain">메인으로 돌아가기</a>
    </div>
    
    <jsp:include page="../footer.jsp" /> 
</body>
</html>
