<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="isOwner" value="${loggedInUserNo eq profile.userNo}" />
<%@ include file="../header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${profile.userNick}의 프로필</title>
<link rel="stylesheet" type="text/css" href="/css/userProfile.css">
<link rel="stylesheet" type="text/css" href="/css/style.css">
<link rel="stylesheet" type="text/css" href="/css/userProfile.css">
</head>
<body>
	<div class="profile-container">
		<div class="profile-header">
			<h1>${profile.userNick}(${profile.userId})의 프로필</h1>
			<button id="favor-btn" data-seller-no="${profile.userNo}">☆</button>
			<button class="openReportWindow" onclick="openReportWindow()" style="margin-left: 800px;">신고하기</button> <!-- syeon -->
		</div>

		<div class="profile-info">
			<div class="info-grid">
				<div class="info-item">
					<span class="info-label">가입일</span>
					<span class="info-value">${profile.formattedDate}</span>
				</div>
				<div class="info-item">
					<span class="info-label">판매중</span>
					<span class="info-value">${profile.sellerItemCnt}</span>
				</div>
				<div class="info-item">
					<span class="info-label">판매평점</span>
					<span class="info-value">${profile.sellerAvgRating}</span>
				</div>
				<div class="info-item">
					<span class="info-label">판매등급</span>
					<span class="info-value">${profile.sellerGrade}</span>
				</div>
			</div>
		</div>

		<div class="profile-tabs">
			<button class="tab-button active" onclick="openTab('sell-tab')">
				판매 아이템
			</button>
			<button class="tab-button" onclick="openTab('buy-tab')">
				구매 아이템
			</button>
			<button class="tab-button" onclick="openTab('dashboard-tab')">
				대시보드
			</button>
		</div>

		<div id="sell-tab" class="tab-content active">
			<div class="items-header">
				<h2>판매 아이템</h2>
				<div class="dropdown">
					<select id="sell-status-filter-options" onchange="changeStatusFilter('sell', this.value)">
						<option value="all" selected>전체 상태</option>
						<c:forEach var="status" items="${itemStatuses}">
							<option value="${status.name().toLowerCase()}">${status.korean}</option>
						</c:forEach>
					</select>
					<select id="sell-sort-options" onchange="changeSorting('sell', this.value)">
						<option value="recent" selected>최신순</option>
						<option value="priceAsc">가격 낮은순</option>
						<option value="priceDesc">가격 높은순</option>
						<option value="titleAsc">제목순</option>
					</select>
				</div>
			</div>
			<div id="sell-items-container">
				<!-- 아이템 목록은 JavaScript로 렌더링 -->
			</div>
			<div id="sell-pagination" class="pagination-container">
				<!-- 페이지네이션은 JavaScript로 렌더링 -->
			</div>
		</div>

		<div id="buy-tab" class="tab-content">
			<div class="items-header">
				<h2>구매 아이템</h2>
				<div class="dropdown">
					<select id="buy-status-filter-options" onchange="changeStatusFilter('buy', this.value)">
						<option value="all" selected>전체 상태</option>
						<c:forEach var="status" items="${itemStatuses}">
							<option value="${status.name().toLowerCase()}">${status.korean}</option>
						</c:forEach>
					</select>
					<select id="buy-sort-options" onchange="changeSorting('buy', this.value)">
						<option value="recent" selected>최신순</option>
						<option value="priceAsc">가격 낮은순</option>
						<option value="priceDesc">가격 높은순</option>
						<option value="titleAsc">제목순</option>
					</select>
				</div>
			</div>
			<div id="buy-items-container">
				<!-- 아이템 목록은 JavaScript로 렌더링 -->
			</div>
			<div id="buy-pagination" class="pagination-container">
				<!-- 페이지네이션은 JavaScript로 렌더링 -->
			</div>
		</div>
		<div id="dashboard-tab" class="tab-content">
			<div class="items-header">
				<h2>대시보드</h2>
			</div>
			<div id="dashboard-container">
				<p>UserProfileDashboardDTO 조회</p>
				<p>- 추가 정보</p>
				<p>- 대시보드 등</p>
			</div>
		</div>
	</div>

	<script src="/js/recent/config.js"></script>
	<script src="/js/recent/getRecent_sidebar.js"></script>
	<script src="/js/favor/favorSeller.js"></script>

	<jsp:include page="../sidebar.jsp" />
	<jsp:include page="../footer.jsp" />

	<script>
		const contextPath = '${pageContext.request.contextPath}';
		const userNo = ${profile.userNo};
	</script>

	<!-- 신고하기 창 열림 by수연 -->
	<script>
	function openReportWindow() {
		const currentUrl = window.location.href;

		const reportWindow = window.open("/report", "ReportWindow", "width=700,height=600,top=200,left=650");

		const sendData = () => {
			reportWindow.postMessage({
				warnWhere: currentUrl,
				warnDefendantUser: "${profile.userNo}"
			}, window.location.origin);
		};

		window.addEventListener("message", function handleAck(event) {
			if (event.origin !== window.location.origin) return;
			if (event.data === "READY_FOR_DATA") {
				sendData();
				window.removeEventListener("message", handleAck); 
			}
		});
	}
	</script>

	<script type="module" src="/js/user/individual/userProfile.js"></script>
</body>
</html>
