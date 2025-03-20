<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>답변목록</title>
</head>
<body>
    <div class="container mt-5">
        <h2>답변목록</h2>

        <!-- 질문 목록을 테이블 형식으로 표시 -->
        <table border="1" cellpadding="10">
            <thead>
                <tr>
                    <th>질문 번호</th>
                    <th>질문 내용</th>
                    <th>답변 내용</th>
                    <th>답변하기</th>
                    <th>삭제하기</th>
                </tr>
            </thead>
            <tbody>
                <!-- 모든 질문에 대해 반복 -->
                <c:forEach var="question" items="${questions}">
                    <tr>
                        <td>${question.questionNo}</td>
                        <td>${question.questionContent}</td>
                        
                        <!-- 질문에 대한 답변 내용 -->
                        <td>
                            <!-- 해당 질문 번호에 해당하는 답변을 찾기 -->
                            <c:set var="hasAnswer" value="false"/>
                            <c:forEach var="answer" items="${answers}">
                                <c:if test="${answer.question.questionNo == question.questionNo}">
                                    <p>${answer.answerContent}</p>
                                    <c:set var="hasAnswer" value="true"/>
                                </c:if>
                            </c:forEach>

                            <!-- 답변이 없으면 "답변 없음" 표시 -->
                            <c:if test="${!hasAnswer}">
                                답변 없음
                            </c:if>
                        </td>

                        <!-- 답변하기 버튼 -->
                        <c:if test="${!hasAnswer}">
                            <td><a href="/question/answer/${question.questionNo}">답변하기</a></td>
                        </c:if>

                        <!-- 삭제 버튼 (답변이 없을 경우에만 삭제 버튼 표시) -->
                        <td>
                            <c:if test="${!hasAnswer}">
                                <form action="/question/delete/${question.questionNo}?from=answerList" method="post" style="display:inline;">
                                    <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
                                </form><br>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
