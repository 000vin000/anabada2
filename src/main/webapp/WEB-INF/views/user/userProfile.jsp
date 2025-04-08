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
<link rel="stylesheet" type="text/css" href="/css/style.css">
<link rel="stylesheet" type="text/css" href="/css/userProfile.css">
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
					<span class="info-value">${profile.formattedDate}</span>
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
			<button class="tab-button logged-in-only" onclick="openTab('etc-tab')" hidden>추가 정보</button>
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
		<div id="etc-tab" class="tab-content">
			<div class="items-header">
				<h2>추가 정보</h2>
			</div>
			<div id="etc-container">
				<p>UserProfileDetailDTO 조회</p>
				<p>- 추가 정보</p>
				<p>- 대시보드 등</p>
			</div>
		</div>
	</div>

	<script type="module">
		import { fetchWithAuth } from '/js/user/fetchWithAuth.js';

		const contextPath = '${pageContext.request.contextPath}';
		const userNo = ${profile.userNo};
		let loggedInUserNo = 0;
		let isOwnProfile = false;
		let sellPage = 0;
		let buyPage = 0;
		let size = 8;
		let sellStatusFilter = 'all';
		let buyStatusFilter = 'all';
		let sellSort = 'recent';
		let buySort = 'recent';
		
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
		
		function changeStatusFilter(type, newStatusFilter) {
		    if (type === 'sell') {
		        sellStatusFilter = newStatusFilter;
		        sellPage = 0;
		        loadSellItems();
		    } else if (type === 'buy') {
		        buyStatusFilter = newStatusFilter;
		        buyPage = 0;
		        loadBuyItems();
		    }
		}
		
		function changeSorting(type, newSort) {
		    if (type === 'sell') {
		        sellSort = newSort;
		        sellPage = 0;
		        loadSellItems();
		    } else if (type === 'buy') {
		        buySort = newSort;
		        buyPage = 0;
		        loadBuyItems();
		    }
		}
		
		function changePage(type, newPage) {
		    if (type === 'sell') {
		        sellPage = newPage;
		        loadSellItems();
		    } else if (type === 'buy') {
		        buyPage = newPage;
		        loadBuyItems();
		    }
		    window.scrollTo({ top: 0, behavior: 'smooth' });
		}

		window.openTab = openTab;
		window.changeStatusFilter = changeStatusFilter;
		window.changeSorting = changeSorting;
		window.changePage = changePage;
		
		document.addEventListener('DOMContentLoaded', async function() {
            const response = await fetchWithAuth('/api/user/profile', { method:"GET" });
			loggedInUserNo = response.ok ? parseInt(await response.text()) : 0;
			isOwnProfile = loggedInUserNo === userNo;

			console.log('로그인 uid: ' + loggedInUserNo);
			console.log('본인프로필 여부: ' + isOwnProfile);

			if (isOwnProfile) {
                document.querySelectorAll('.logged-in-only').forEach(elem => {
                    elem.hidden = false;
                });
            }

		    loadSellItems();
		});
		
		function loadSellItems() {
		    document.getElementById('sell-items-container').innerHTML = '<div class="loading-message">판매 아이템 로딩 중</div>';
		    
		    const url = '/api/user/profile/' + userNo + '/sells?page=' + sellPage + '&size=' + size
		    		+ '&status=' + sellStatusFilter + '&sort=' + sellSort;
		    fetchWithAuth(url, { method: "GET" })
		        .then(response => response.json())
			    .then(data => {
			        displayItems(data, 'sell-items-container');
			        displayPagination(data, 'sell-pagination', 'sell');
			    })
		        .catch(error => {
		            console.error('오류 발생:', error);
		            document.getElementById('items-container').innerHTML = '<div class="empty-message">데이터 로드 실패</div>';
		        });
		}
		
		function loadBuyItems() {
		    document.getElementById('buy-items-container').innerHTML = '<div class="loading-message">구매 아이템 로딩 중</div>';
		    
		    const url = '/api/user/profile/' + userNo + '/buys?page=' + buyPage + '&size=' + size
		    		+ '&status=' + buyStatusFilter + '&sort=' + buySort;
		    fetchWithAuth(url, { method: "GET" })
		        .then(response => response.json())
		        .then(data => {
		            displayItems(data, 'buy-items-container');
		            displayPagination(data, 'buy-pagination', 'buy');
		        })
		        .catch(error => {
		            console.error('오류 발생:', error);
		            document.getElementById('items-container').innerHTML = '<div class="empty-message">데이터 로드 실패</div>';
		        });
		}
		
		function displayPagination(data, containerId, type) {
		    const container = document.getElementById(containerId);
		    container.innerHTML = '';
		    
		    if (!data || !data.totalPages) {
		        return;
		    }
		    
		    const totalPages = data.totalPages;
		    const currentPage = data.number;
		    
		    const ul = document.createElement('ul');
		    ul.className = 'pagination';
		    
		    const prevLi = document.createElement('li');
		    const prevLink = document.createElement('a');
		    prevLink.href = '#';
		    prevLink.innerHTML = '&laquo;';
		    
		    if (currentPage === 0) {
		        prevLink.className = 'disabled';
		    } else {
		        prevLink.onclick = function(e) {
		            e.preventDefault();
		            changePage(type, currentPage - 1);
		        };
		    }
		    
		    prevLi.appendChild(prevLink);
		    ul.appendChild(prevLi);
		    
		    const startPage = Math.max(0, currentPage - 2);
		    const endPage = Math.min(totalPages - 1, currentPage + 2);
		    
		    for (let i = startPage; i <= endPage; i++) {
		        const pageLi = document.createElement('li');
		        const pageLink = document.createElement('a');
		        pageLink.href = '#';
		        pageLink.textContent = i + 1;
		        
		        if (i === currentPage) {
		            pageLink.className = 'active';
		        } else {
		            pageLink.onclick = function(e) {
		                e.preventDefault();
		                changePage(type, i);
		            };
		        }
		        
		        pageLi.appendChild(pageLink);
		        ul.appendChild(pageLi);
		    }
		    
		    const nextLi = document.createElement('li');
		    const nextLink = document.createElement('a');
		    nextLink.href = '#';
		    nextLink.innerHTML = '&raquo;';
		    
		    if (currentPage === totalPages - 1) {
		        nextLink.className = 'disabled';
		    } else {
		        nextLink.onclick = function(e) {
		            e.preventDefault();
		            changePage(type, currentPage + 1);
		        };
		    }
		    
		    nextLi.appendChild(nextLink);
		    ul.appendChild(nextLi);
		    
		    container.appendChild(ul);
		}
		
		function displayItems(data, containerId) {
			const container = document.getElementById(containerId);
		    container.innerHTML = '';

		    if (!data || !data.content || data.content.length === 0) {
		        container.innerHTML = '<div class="empty-message">아이템이 없습니다.</div>';
		        return;
		    }

		    const grid = document.createElement('div');
		    grid.className = 'items-grid';

		    data.content.forEach(item => {
		        const imageUrl = contextPath + '/image/' + item.itemNo;
		        const detailUrl = contextPath + '/item/detail/' + item.itemNo;

		        const link = document.createElement('a');
		        link.href = detailUrl;
		        link.className = 'item-card-link';

		        const itemCardDiv = document.createElement('div');
		        itemCardDiv.className = 'item-card';

		        const img = document.createElement('img');
		        img.src = imageUrl;
		        img.alt = item.itemTitle;
		        img.className = 'item-image';
		        img.onerror = () => {
		            img.alt = '이미지가 없습니다';
		        };

		        const infoDiv = document.createElement('div');
		        infoDiv.className = 'item-details';
		        
		        const statusDiv = document.createElement('div');
                statusDiv.className = 'item-status '
                	+ (item.itemStatus !== '대기중' && item.itemStatus === '판매중' ? 'status-selling' : 'status-sold');
                statusDiv.textContent = item.itemStatus;

		        const titleDiv = document.createElement('div');
		        titleDiv.className = 'item-title';
		        titleDiv.textContent = item.itemTitle;

		        const priceDiv = document.createElement('div');
		        priceDiv.className = 'item-price';
		        priceDiv.textContent = item.formattedPrice + '원';
		        
		        const metaDiv = document.createElement('div');
                metaDiv.className = 'item-meta';
                
                const viewsDiv = document.createElement('div');
                viewsDiv.className = 'item-meta-item';
                viewsDiv.innerHTML = '조회 ' + item.viewCount;
                
                const bidsDiv = document.createElement('div');
                bidsDiv.className = 'item-meta-item';
                bidsDiv.innerHTML = '입찰 ' + item.bidCount;

                metaDiv.appendChild(viewsDiv);
                metaDiv.appendChild(bidsDiv);
                
                if (item.itemSoldDate != null) {
                    const dateDiv = document.createElement('div');
                    dateDiv.className = 'item-meta-item';
                    dateDiv.innerHTML = '낙찰 ' + item.formattedDate;
                    metaDiv.appendChild(dateDiv);
                }
                
                infoDiv.appendChild(statusDiv);
                infoDiv.appendChild(metaDiv);
		        infoDiv.appendChild(titleDiv);
		        infoDiv.appendChild(priceDiv);

				if (isOwnProfile && item.reviewed) {
					const reviewDiv = document.createElement('review');
					reviewDiv.className = 'item-review logged-in-only';
					reviewDiv.innerHTML = '<button>리뷰</button>';
					infoDiv.appendChild(reviewDiv);
				}

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
