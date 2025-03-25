<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <title>고객센터</title>
    
      <style>
         nav {
            margin-bottom: 20px;
        }
        nav ul {
            list-style: none;
            display: flex;
            justify-content: center;
            margin: 0;
            padding: 0;
        }
        nav ul li {
            margin: 0 15px;
        }
        nav ul li a {
            color: #007bff;
            text-decoration: none;
            font-size: 18px;
        }
        nav ul li a:hover {
            text-decoration: underline;
        }
        </style>

 </head>
<body>
 <nav>
        <ul>
            <!-- '재무관리' 탭을 대시보드로 연결 -->
            <li><a href="/dashboard">재무관리</a></li> <!-- 대시보드 페이지로 연결 -->
            <li><a href="/management">고객관리</a></li>
        </ul>
    </nav>
    <h1>고객센터</h1>

      <!-- 처리 결과 메시지 -->
    <c:if test="${not empty message}">
        <div class="alert">
            ${message}
        </div>
    </c:if>

    <div id="report-table" style="display:block;">
        <h2>신고 목록</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>신고 번호</th>
                    <th>사용자 ID</th>
                    <th>경고 사유</th>
                    <th>경고 상태</th>
                    <th>신고 생성일</th>
                    <th>신고 처리일</th>
                    <th>승인하기</th>
                    <th>거부하기</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="warn" items="${warns}">
                    <tr>
                        <td>${warn.warnNo}</td>
                        <td>${warn.user.userId}</td>
                        <td>${warn.warnReason}</td>
                        <td>${warn.warnStatus}</td>
                        <td>${warn.warnCreatedDate}</td>
                        <td>${warn.warnProcessedDate != null ? warn.warnProcessedDate : '처리 안됨'}</td>
                        <td>
                            <c:if test="${warn.warnStatus == 'REQUESTED'}">
                                <form action="/management/approve/${warn.warnNo}" method="post" style="display:inline;">
                                    <button type="submit" onclick="return confirm('정말 승인하시겠습니까?')">승인하기</button>
                                </form>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${warn.warnStatus == 'REQUESTED'}">
                                <form action="/management/reject/${warn.warnNo}" method="post" style="display:inline;">
                                    <button type="submit" onclick="return confirm('정말 거부하시겠습니까?')">거부하기</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="container mt-5">
        <h2>질문 목록</h2>
        <table border="1" cellpadding="10">
            <thead>
                <tr>
                    <th>질문 번호</th>
                    <th>질문자 ID</th>
                    <th>질문 제목</th>
                    <th>질문 내용</th>
                    <th>답변 내용</th>
                    <th>답변하기</th>
                    <th>삭제하기</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="question" items="${questions}">
                    <tr>
                        <td>${question.questionNo}</td>
                        <td>${question.sender.userId}</td>
                        <td>${question.questionTitle}</td>
                        <td>${question.questionContent}</td>
                        <td>
                <c:set var="answers" value="${answersByQuestionNo[question.questionNo]}" />
                <c:choose>
                    <c:when test="${not empty answers}">
                        <c:forEach var="answer" items="${answers}">
                            ${answer.answerContent}<br>
                        </c:forEach>
                    </c:when>
                   		<c:otherwise>답변 없음</c:otherwise>
                </c:choose>
           		 </td>
                     	 <td>
                           <c:if test="${empty answers}">
                                <a href="/question/answer/${question.questionNo}">답변하기</a>
                            </c:if>
                        </td>
                        <td>
                             <c:if test="${empty answers}">
                                <form action="/question/delete/${question.questionNo}?from=answerList" method="post" style="display:inline;">
                                    <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <!-- 공지사항 목록 -->
    <div class="container mt-5">
        <h2>공지사항 목록</h2>
        <a href="/notice/create" style="display: inline-block; margin-bottom: 15px;">공지사항 등록하기</a>
        <table border="1" cellpadding="10">
            <thead>
                <tr>
                    <th>공지사항 번호</th>
                    <th>제목</th>
                    <th>내용</th>
                    <th>수정하기</th>
                    <th>삭제하기</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="notices" items="${notices}">
                    <tr>
                        <td>${notices.noticeNo}</td>
                        <td>${notices.noticeTitle}</td>
                        <td>${notices.noticeContent}</td>
                        <td>
                            <a href="/notice/edit/${notices.noticeNo}">수정하기</a>
                        </td>
                        <td>
                            <form action="/notice/delete/${notices.noticeNo}" method="post" style="display:inline;">
                                <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    

</body>
</html>
