<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <title>고객센터</title>
    <link rel="stylesheet" href="/css/styleAdmin.css" />
    <link rel="stylesheet" href="/css/styleWarnModal.css"/>
 </head>
<body>
    <!-- 네비게이션 바 -->
    <nav>
        <ul>
            <li><a href="/">홈</a></li> 
            <li><a href="/admin/dashboard">재무관리</a></li>
            <li><a href="/admin/management">고객관리</a></li>
            <li><a href="/admin/fees">수수료관리</a></li>
            <li><a href="/admin/acceptConversion">코인 전환 신청</a></li>
            <li><a href="/admin/depositWithdrawal">입/출금 관리</a></li>
            <li><a href="/admin/adminRole">관리자 권한 설정</a></li>
        </ul>
    </nav>

    <!-- 메인 콘텐츠 컨테이너 -->
    <div class="container">
        <h1>고객센터</h1>

        <!-- 처리 메시지 -->
        <c:if test="${not empty message}">
            <div class="alert">${message}</div>
        </c:if>

        <!-- 신고 목록 -->
        <div id="report-table">
            <h2>신고 접수 목록</h2>
            <table border="1">
                <thead>
                    <tr>
                        <th>신고 번호</th>
                        <th>신고한 유저 아이디</th>
                        <th>신고당한 유저 아이디</th>
                        <th>신고 접수일</th>
                        <th>신고 사유</th>
                        <th>상세보기</th>
                        <th>처리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="warn" items="${warns}" varStatus="status">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td>${warn.warnPlaintiffUser.userId}</td>
                            <td>${warn.warnDefendantUser.userId}</td>
                            <td>${warn.formatDateTime(warn.warnCreatedDate)}</td>
                            <td>
                            	<c:if test="${warn.warnReason != 'OTHER' }">
                            		${warn.warnReason.getKorean()}
                            	</c:if>
                            	<c:if test="${warn.warnReason == 'OTHER' }">
									<span class="showWarnReasonDetailBtn" data-detail="${warn.warnReasonDetail}" role="button" tabindex="0">
									    ${warn.warnReason.getKorean()}
									</span>
                            	</c:if>
                            </td>
							<td>
								<c:if test="${warn.warnWhere == 'PROFILE'}">
									<a href="#">프로필 링크</a>
								</c:if>
								<c:if test="${warn.warnWhere == 'ITEM'}">
									<a href="/item/detail/${warn.warnItem.itemNo}" target="_blank" rel="noopener noreferrer">${warn.warnItem.itemTitle}</a>
								</c:if>
								<c:if test="${warn.warnWhere == 'CHATTING'}">
									<a href="#">채팅방 링크</a>	
								</c:if>
							</td>
							<th>
								<button onclick="openWarnSetResultModal('${warn.warnNo}')">승인</button>
								<button class="deleteWarn" data-warn-no="${warn.warnNo}">삭제</button>
							</th>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 질문 목록 -->
        <div class="mt-5">
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
                                    <button type="button" class="delete-btn" data-question-no="${question.questionNo}">삭제하기</button>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 공지사항 목록 -->
        <div class="mt-5">
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
                            <td><a href="/notice/edit/${notices.noticeNo}">수정하기</a></td>
                            <td>
                                <button type="button" class="delete-notice-btn" data-notice-no="${notices.noticeNo}">삭제하기</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 탈퇴 신청 목록 -->
        <div class="mt-5">
            <h2>탈퇴 신청 목록</h2>
            <table border="1" cellpadding="10">
                <thead>
                    <tr>
                        <th>일련번호</th>
                        <th>사용자 ID</th>
                        <th>이메일</th>
                        <th>탈퇴 타입</th>
                        <th>탈퇴 사유</th>
                        <th>생성일자</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="withdrawal" items="${withdrawals}">
                        <tr>
                            <td>${withdrawal.withdrawalNo}</td>
                            <td>${withdrawal.user.userId}</td>
                            <td>${withdrawal.withdrawalEmail}</td>
                            <td>${withdrawal.withdrawalType}</td>
                            <td>${withdrawal.withdrawalReason}</td>
                            <td>${withdrawal.withdrawalCreatedDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

	<!-- 신고 상세 내용 모달 -->
	<div id="warnReasonModal" style="display: none;">
	    <div id="warnReasonText"></div>
	    <div id="warnReasonModalFooter">
	        <button onclick="closeModal('warnReasonModal')">닫기</button>
	    </div>
	</div>

	<!-- 신고 처리 모달 -->
	<div id="warnResultModal" style="display: none;">
	    <div id="warnResultRadioBtn">
	        <label for="suspension">
	            <input type="radio" id="suspension" name="warnResult" value="SUSPENSION" onchange="toggleSuspensionDays()"> 정지
	        </label>
	        <label for="permanentStop">
	            <input type="radio" id="permanentStop" name="warnResult" value="PERMANENTSTOP" onchange="toggleSuspensionDays()"> 영구정지
	        </label>
	    </div>
	
	    <div id="setWarnResultDays" style="display: none;">
	        <input type="number" id="warnSuspensionDays" value="1" min="1"> 일
	    </div>
	
	    <div id="warnResultModalFooter">
	        <button class="submitWarnResult" data-warn-no="${warn.warnNo}">확인</button>
	        <button onclick="closeModal('warnResultModal')">닫기</button>
	    </div>
	</div>

    <!-- 스크립트 -->
    <script type="module" src="/js/admin/customerManagement.js"></script>
</body>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const buttons = document.querySelectorAll(".showWarnReasonDetailBtn");
        const modal = document.getElementById("warnReasonModal");
        const modalText = document.getElementById("warnReasonText");

        buttons.forEach(button => {
            button.addEventListener("click", () => {
                const detail = button.getAttribute("data-detail");
                modalText.textContent = detail || "";
                
                modal.style.display = "block";
            });
        });
        
        const input = document.getElementById("warnSuspensionDays");

        input.addEventListener("input", function () {
            if (parseInt(this.value) < 1) {
                this.value = 1;
            }
        });
    });

    function closeModal(modal) {
        document.getElementById(modal).style.display = "none";
    }
    
    function openWarnSetResultModal(warnNo) {
        document.getElementById("warnResultModal").style.display = "block";
        document.querySelector(".submitWarnResult").setAttribute("data-warn-no", warnNo);
    }
    
    function toggleSuspensionDays() {
        const suspensionOption = document.getElementById("suspension");
        const suspensionDaysInput = document.getElementById("setWarnResultDays");

        if (suspensionOption.checked) {
            suspensionDaysInput.style.display = "block";
        } else {
            suspensionDaysInput.style.display = "none";
        }
    }
    
    function openDeleteWarn(warnNo) {
    	const confirmed = confirm("정말 삭제하시겠습니까?");
    	if (confirmed) {
			fetch(`/warn/deleteWarn/${warnNo}`, {
				
			});
    	    console.log("신고 처리 실행!");
    	} else {
    	    // 취소 버튼을 눌렀을 때 실행할 코드
    	    console.log("신고 취소됨");
    	}
    }
</script>
</html>
