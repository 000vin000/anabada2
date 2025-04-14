<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${item.itemTitle}</title>
<link rel="stylesheet" type="text/css" href="/css/itemDetail.css">
<link rel="stylesheet" type="text/css" href="/css/style.css">
<link rel="stylesheet" type="text/css" href="/css/chatRoom.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>
</head>
<body>
	<div class="item-detail-container">
		<div class="item-header">
			<div class="item-title-section">
				<h1 class="item-name">${item.itemTitle}</h1>
				<button id="favor-btn" data-item-no="${item.itemNo}">☆</button>
			</div>
			<div class="item-action-buttons">
				<!-- 숨김 버튼 (JS에서 조건 분기) -->
				<button id="edit-btn" class="action-btn logged-in-only" hidden>수정</button>
				<button id="delete-btn" class="action-btn logged-in-only" hidden>삭제</button>

				<!-- 판매자 번호를 JS에서 사용할 수 있도록 숨겨둠 -->
				<p id="sellerNo" style="display: none;">${item.sellerNo}</p>

				<!-- 버튼은 모두 숨겨두고 JS에서 분기 제어 -->
				<button id="viewChatRoomsBtn" class="viewChatRoomsBtn" data-item-no="${item.itemNo}" style="display: none;">
				    문의목록
				</button>

				<button id="inquiryBtn" class="inquiryBtn" data-item-no="${item.itemNo}" style="display: none;">
				    문의하기
				</button>
				
				<!-- 신고하기 by수연-->
				<button class="openReportWindow" onclick="openReportWindow()">신고하기</button>
				
				<!-- 채팅 목록 모달창 -->
				<div id="chatRoomModal" class="modal-overlay" style="display: none;">
				  <div class="modal-content">
				    <span class="modal-close" id="closeChatModal">&times;</span>
				    <h2>채팅방 목록</h2>
				    <div id="chat-room-list-box"></div>
				  </div>
				</div>
			</div>
		</div>

		<div class="item-content">
			<div class="item-gallery">
				<c:choose>
					<c:when test="${item.imageCount > 0}">
						<img id="main-image" class="gallery-main-image"
							 src="/image/${item.itemNo}/0" alt="메인 이미지">
						<div class="gallery-thumbnails">
							<c:forEach begin="0" end="${item.imageCount-1}" var="index">
								<img src="/image/${item.itemNo}/${index}" alt="메인 이미지"
									 class="gallery-thumbnail" onclick="changeMainImage(this.src)">
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div class="gallery-main-image no-image"
							 style="display: flex; align-items: center; justify-content: center; color: #999">
							이미지가 없습니다
						</div>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="item-info-section">
				<div id="time-section" class="time-section"
					 ${item.itemStatus ne '대기중' and item.itemStatus ne '판매중' or empty item.itemSaleEndDate ? 'hidden' : ''}>
					<span id="remain-time-heading">경매 시간</span>
					<span id="remain-time">계산 중</span>
				</div>

				<div class="bid-section">
					<h2 id="price-heading">${item.itemStatus}</h2>
					<span id="price">${item.getFormattedPrice(item.itemPrice)} 원</span>

					<div id="price-input-section" ${item.itemStatus ne '판매중' ? 'hidden' : ''}>
						<br>
						<h2 id="price-heading">희망 입찰가</h2>
						<div class="bid-input-group">
							<input type="number" value="${item.itemPrice.toBigInteger() + 1000}"
							id="price-text" min="0" step="1000" placeholder="입찰 금액을 입력하세요">
							<input type="button" id="bid-btn" value="입찰">
						</div>
					</div>
					<button onclick="openWindow('BidlistWindow', '/bidList/${item.itemNo}')" id="bid-list">
						입찰 기록 보기
					</button>
				</div>
			</div>
		</div>

		<div class="details-section">
			<h3>상품 상세 정보</h3>
			<div class="details-grid">
				<div class="detail-item">
					<span class="detail-label">판매자</span>
					<span class="detail-value">
						<a href="${pageContext.request.contextPath}/user/profile/${item.sellerNo}">${item.sellerNick}</a>
					</span>
				</div>
				<div class="detail-item">
					<span class="detail-label">카테고리</span>
					<span class="detail-value">${item.categoryName}</span>
				</div>
				<div class="detail-item">
					<span class="detail-label">경매 기간</span>
					<span class="detail-value">
						${item.getFormattedDate(item.itemSaleStartDate)} ~
						<c:choose>
							<c:when test="${item.itemSaleEndDate ne null}">
								${item.getFormattedDate(item.itemSaleEndDate)}
							</c:when>
							<c:otherwise>
								무기한
							</c:otherwise>
						</c:choose>
						<span id="status">${item.itemStatus}</span>
					</span>
				</div>
				<c:if test="${not empty item.itemQuality}">
					<div class="detail-item">
						<span class="detail-label">품질</span>
						<span class="detail-value">${item.itemQuality}</span>
					</div>
				</c:if>
				<div class="detail-content">
					<c:out value="${item.itemContent}" escapeXml="false" />
				</div>
			</div>
		</div>

		<button onclick="window.history.back()" class="back-button">뒤로가기</button>
	</div>

	<!-- 공통 모듈 -->
	<jsp:include page="../sidebar.jsp" />
	<jsp:include page="../footer.jsp" />

	<!-- JS 설정 -->
	<script src="/js/recent/config.js"></script>
	<script src="/js/recent/addRecent.js"></script>
	<script src="/js/recent/getRecent_sidebar.js"></script>
	<script src="/js/favor/favorItem.js"></script>
	<script type="module" src="/js/chat/chatRoom.js"></script>
	<script type="module" src="/js/chat/chatRoomList.js"></script>
	<script type="module" src="/js/chat/itemDetail.js"></script>

	<!-- JS에서 사용할 변수 정의 -->
	<script>
		const itemNo = ${item.itemNo};
		const sellerNo = ${item.sellerNo};
		const initialItemStatus = "${item.itemStatus}";
		const initialItemSaleEndDate = ${not empty item.itemSaleEndDate ? '"' += item.itemSaleEndDate.toString() += '"' : 'null'};
	</script>

	<!-- 메인 스크립트 -->
	<script type="module" src="/js/item/itemDetail.js"></script>
	<script type="module">
		import { openWindow } from '/js/item/itemDetailUtil.js';
		window.openWindow = openWindow;
	</script>
	
	<!-- 신고하기 창 열림 by수연 -->
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
