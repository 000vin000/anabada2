<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고객센터</title>
<style>
    .tabs {
        display: flex;
        border-bottom: 2px solid #ddd;
    }
    .tab {
        padding: 10px 20px;
        cursor: pointer;
        margin-right: 10px;
    }
    .tab:hover {
        background-color: #f0f0f0;
    }
    .tab.active {
        background-color: #007bff;
        color: white;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }
    table th, table td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }
</style>
<script>
    function switchTab(tabName) {
        // 모든 탭을 비활성화
        document.querySelectorAll('.tab').forEach(function(tab) {
            tab.classList.remove('active');
        });

        // 선택된 탭을 활성화
        document.getElementById(tabName).classList.add('active');
        
        // 모든 테이블을 숨기고, 선택된 테이블만 표시
        document.querySelectorAll('.tab-content').forEach(function(content) {
            content.style.display = 'none';
        });
        
        document.getElementById(tabName + '-table').style.display = 'block';
    }
</script>
</head>
<body>

    <h1>고객센터</h1>

    <!-- 탭 메뉴 -->
    <div class="tabs">
        <div class="tab active" id="brand" onclick="switchTab('brand')">브랜드 계정</div>
        <div class="tab" id="personal" onclick="switchTab('personal')">개인 계정</div>
    </div>

    <!-- 브랜드 계정 신고 관리 -->
    <div class="tab-content" id="brand-table" style="display:block;">
        <h2>브랜드 계정 신고 관리</h2>
        <table>
            <thead>
                <tr>
                    <th>신고 번호</th>
                    <th>유저 이름</th>  <!-- 유저 번호 대신 유저 이름으로 표시 -->
                    <th>경고 사유</th>
                    <th>경고 상태</th>
                    <th>신고 생성일</th>
                    <th>신고 처리일</th>
                    <th>처리하기</th>
                </tr>
            </thead>
            <tbody>
                <!-- 브랜드 신고 목록을 반복문으로 표시 -->
                <c:forEach var="report" items="${brandReports}">
                    <tr>
                        <td>${report.warnNo}</td>
                        <td>${report.userName}</td> <!-- userName을 추가하여 유저 이름 표시 -->
                        <td>${report.warnReason}</td>
                        <td>${report.warnStatus}</td>
                        <td>${report.warnCreatedDate}</td>
                        <td>${report.warnProcessedDate != null ? report.warnProcessedDate : '처리 안됨'}</td> <!-- 처리일자가 있으면 표시, 없으면 '처리 안됨' 표시 -->
                        <td><a href="/report/handle/${report.warnNo}">처리하기</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- 개인 계정 신고 관리 -->
    <div class="tab-content" id="personal-table" style="display:none;">
        <h2>개인 계정 신고 관리</h2>
        <table>
            <thead>
                <tr>
                    <th>신고 번호</th>
                    <th>유저 이름</th>
                    <th>경고 사유</th>
                    <th>경고 상태</th>
                    <th>신고 생성일</th>
                    <th>신고 처리일</th>
                    <th>처리하기</th>
                </tr>
            </thead>
            <tbody>
                <!-- 개인 신고 목록을 반복문으로 표시 -->
                <c:forEach var="report" items="${personalReports}">
                    <tr>
                        <td>${report.warnNo}</td>
                        <td>${report.userName}</td> <!-- userName을 추가하여 유저 이름 표시 -->
                        <td>${report.warnReason}</td>
                        <td>${report.warnStatus}</td>
                        <td>${report.warnCreatedDate}</td>
                        <td>${report.warnProcessedDate != null ? report.warnProcessedDate : '처리 안됨'}</td>
                        <td><a href="/report/handle/${report.warnNo}">처리하기</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <!-- 고객센터 -->
    <div class="container mt-5">
        <h2>고객센터</h2>

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
