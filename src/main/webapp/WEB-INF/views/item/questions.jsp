<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문의하기</title>
</head>
<body>
<h2>문의하기</h2>
		<div>
			<table border="1" style="width:1000px; text-align:center; border-collapse:collapse;">
			    <thead style="background-color:#f0f0f0;">
			        <tr>
			            <th>제목</th>
			            <th>내용</th>
			            <th>작성일</th>
			        </tr>
			    </thead>
			    <tbody>
			        <c:forEach var="q" items="${questions}">
			            <tr>
			                <td>${q.QTitle}</td>
			                <td>${q.QContent}</td>
			                <td>${q.QDate}</td>
			            </tr>
			        </c:forEach>
			    </tbody>
			</table>
		</div>
		<div>
			<textarea rows="10" cols="35"></textarea>
			<input type="button" id="btnQ" value="문의">
		</div>
</body>
</html>