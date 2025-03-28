<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${profile.userNick}의 프로필</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
	<div class="profile-container">
		<div class="profile-header">
			<h1>${profile.userNick}(${profile.userId})의 프로필</h1>
			<button id="favor-btn" data-seller-no="${profile.userNo}">☆</button>
		</div>

		<div class="profile-info">
			<div class="info-grid">
				<div class="info-item">
					<span class="info-label">가입일</span>
					<span class="info-value">${profile.getFormattedDate()}</span>
				</div>
				<div class="info-item">
					<span class="info-label">판매수</span>
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
			<button class="tab-button active" onclick="openTab('sellingTab')">판매 아이템</button>
			<button class="tab-button" onclick="openTab('boughtTab')">구매 아이템</button>
		</div>

		<div id="sellingTab" class="tab-content active">
			<div class="items-header">
				<h2>판매 아이템</h2>
				<a href="#" class="view-all">전체보기</a>
			</div>

			<div class="items-grid">
				<c:forEach items="${profile.sellSummaryDTOs}" var="item">
					<a href="${pageContext.request.contextPath}/item/detail/${item.itemNo}" class="item-card-link">
						<div class="item-card">
							<img src="/image/${item.itemNo}" alt="${item.itemTitle}" class="item-image">
							<div class="item-details">
								<div class="item-title">${item.itemTitle}</div>
								<div class="item-price">${item.getFormattedPrice()}원</div>
							</div>
						</div>
					</a>
				</c:forEach>
			</div>
		</div>

		<div id="boughtTab" class="tab-content">
			<div class="items-header">
				<h2>구매 아이템</h2>
			</div>
			<div class="items-grid">
			
			</div>
		</div>
	</div>

	<jsp:include page="../sidebar.jsp" />
	<jsp:include page="../footer.jsp" />
</body>
<script>
    function openTab(tabId) {
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.classList.remove('active');
        });
        
        document.querySelectorAll('.tab-button').forEach(button => {
            button.classList.remove('active');
        });
        
        document.getElementById(tabId).classList.add('active');
        
        document.querySelector('.tab-button[onclick="openTab(\'' + tabId + '\')"]').classList.add('active');
    }
</script>
<script src="/js/recent/config.js"></script>
<script src="/js/recent/getRecent_sidebar.js"></script>
<script src="/js/favor/favorSeller.js"></script>
</html>
