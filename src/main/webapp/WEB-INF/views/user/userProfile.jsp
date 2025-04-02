<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="isLoggedIn" value="${userNo ne 0}" />
<c:set var="isOwner" value="${userNo eq profile.userNo}" />
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
            <button class="tab-button active" onclick="openTab('sell-tab')">판매 아이템</button>
            <button class="tab-button" onclick="openTab('buy-tab')">구매 아이템</button>
        </div>

        <div id="sell-tab" class="tab-content active">
            <div class="items-header">
                <h2>판매 아이템</h2>
                <div class="sort-dropdown">
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
        </div>

		<div id="buy-tab" class="tab-content">
            <div class="items-header">
                <h2>구매 아이템</h2>
                <div class="sort-dropdown">
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
        </div>
    </div>
	
	<script>
		const contextPath = '${pageContext.request.contextPath}';
		const userNo = ${profile.userNo};
		let page = 0;
		let size = 8;
		let sort = 'recent';
		
		document.addEventListener('DOMContentLoaded', function() {
		    loadSellItems();
		});
		
		function openTab(tabId) {
	        document.querySelectorAll('.tab-content').forEach(tab => {
	            tab.classList.remove('active');
	        });
	        
	        document.querySelectorAll('.tab-button').forEach(button => {
	            button.classList.remove('active');
	        });
	        
	        document.getElementById(tabId).classList.add('active');
	        document.querySelector('.tab-button[onclick="openTab(\'' + tabId + '\')"]').classList.add('active');
	        
	        if (tabId === 'sell-tab') {
	            loadSellItems();
	        } else if (tabId === 'buy-tab') {
	            loadBuyItems();
	        }
	    }
		
		function loadSellItems() {
		    document.getElementById('sell-items-container').innerHTML = '<p>판매 아이템 로딩 중</p>';
		    
		    const url = '/user/profile/' + userNo + '/sells?page=' + page + '&size=' + size + '&sort=' + sort;
		    fetch(url)
		        .then(response => response.json())
			    .then(data => {
			        displayItems(data, 'sell-items-container');
			    })
		        .catch(error => {
		            console.error('오류 발생:', error);
		            document.getElementById('items-container').innerHTML = '<p>데이터 로드 실패</p>';
		        });
		}
		
		function loadBuyItems() {
		    document.getElementById('buy-items-container').innerHTML = '<p>구매 아이템 로딩 중</p>';
		    
		    const url = '/user/profile/' + userNo + '/buys?page=' + page + '&size=' + size + '&sort=' + sort;
		    fetch(url)
		        .then(response => response.json())
		        .then(data => {
		            displayItems(data, 'buy-items-container');
		        })
		        .catch(error => {
		            console.error('오류 발생:', error);
		            document.getElementById('items-container').innerHTML = '<p>데이터 로드 실패</p>';
		        });
		}
		
		function displayItems(data, containerId) {
			const container = document.getElementById(containerId);
		    container.innerHTML = '';

		    if (!data || !data.content || data.content.length === 0) {
		        container.innerHTML = '<p>아이템이 없습니다.</p>';
		        return;
		    }

		    const grid = document.createElement('div');
		    grid.className = 'items-grid';

		    data.content.forEach(item => {
		        const itemNo = item.itemNo;
		        const itemTitle = item.itemTitle;
		        const formattedPrice = item.formattedPrice;
		        const imageUrl = contextPath + '/image/' + itemNo;
		        const detailUrl = contextPath + '/item/detail/' + itemNo;

		        if (itemNo === undefined || itemTitle === undefined || formattedPrice === undefined) {
		             return;
		        }

		        const link = document.createElement('a');
		        link.href = detailUrl;
		        link.className = 'item-card-link';

		        const itemCardDiv = document.createElement('div');
		        itemCardDiv.className = 'item-card';

		        const img = document.createElement('img');
		        img.src = imageUrl;
		        img.alt = itemTitle;
		        img.className = 'item-image';
		        img.onerror = () => {
		            img.alt = '이미지가 없습니다';
		        };

		        const infoDiv = document.createElement('div');
		        infoDiv.className = 'item-details';

		        const titleDiv = document.createElement('div');
		        titleDiv.className = 'item-title';
		        titleDiv.textContent = itemTitle;

		        const priceDiv = document.createElement('div');
		        priceDiv.className = 'item-price';
		        priceDiv.textContent = formattedPrice + '원';

		        infoDiv.appendChild(titleDiv);
		        infoDiv.appendChild(priceDiv);
		        itemCardDiv.appendChild(img);
		        itemCardDiv.appendChild(infoDiv);
		        link.appendChild(itemCardDiv);
		        grid.appendChild(link);
		    });

		    container.appendChild(grid);
		}
	</script>
	
	<script src="/js/recent/config.js"></script>
	<script src="/js/recent/getRecent_sidebar.js"></script>
	<script src="/js/favor/favorSeller.js"></script>

	<jsp:include page="../sidebar.jsp" />
	<jsp:include page="../footer.jsp" />
</body>
</html>
