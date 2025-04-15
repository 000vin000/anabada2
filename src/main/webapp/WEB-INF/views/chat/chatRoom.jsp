<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>채팅방</title>
    <link rel="stylesheet" type="text/css" href="/css/chatRoom.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    
</head>
<body>
    <h1>채팅방 - Room No: ${roomNo}</h1>

    <div id="chat-room" data-roomNo="${roomNo}" data-userNo="${loggedInUserNo}"></div>

    <div id="chat-box">

    </div>

    <textarea id="message-input" rows="2" placeholder="메시지를 입력하세요"></textarea>
    <button id="send-message">전송</button>
    
	<!-- 신고하기 by수연(정빈 옮겨옴)-->
	<button class="openReportWindow" onclick="openReportWindow()">신고하기</button>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script type="module" src="/js/chat/chatRoom.js"></script>
    
    <!-- 신고하기 창 열림 by수연(정빈 옮겨옴) -->
	<script> 
	function openReportWindow() {
		const currentUrl = window.location.href;

		const reportWindow = window.open("/report", "ReportWindow", "width=700,height=600,top=200,left=650");

		const sendData = () => {
			reportWindow.postMessage({
				warnWhere: currentUrl,
				warnDefendantUser: "${item.sellerNo}",
				warnItem: "${item.itemNo}"
			}, window.location.origin);
		};

		window.addEventListener("message", function handleAck(event) {
			if (event.origin !== window.location.origin) return;
			if (event.data === "READY_FOR_DATA") {
				sendData();
				window.removeEventListener("message", handleAck); // 한 번만 전송
			}
		});
	}
	</script>
</body>
</html>
