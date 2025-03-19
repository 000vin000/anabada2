<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고객센터 문의 작성</title>
</head>
<body>
<h2>고객센터 문의 작성</h2>
<form action="/question/write" method="post">
    <label for="questionTitle">제목:</label> <!-- 필드 이름 수정 -->
    <input type="text" id="questionTitle" name="questionTitle" required /><br> <!-- 필드 이름 수정 -->

    <label for="questionContent">내용:</label><br> <!-- 필드 이름 수정 -->
    <textarea id="questionContent" name="questionContent" rows="5" cols="40" required></textarea><br> <!-- 필드 이름 수정 -->

    <label for="qIsPrivate">비밀글 여부:</label>
    <input type="checkbox" id="qIsPrivate" name="qIsPrivate" value="true" /><br>

    <input type="submit" value="문의하기" />
</form>
</body>
</html>
