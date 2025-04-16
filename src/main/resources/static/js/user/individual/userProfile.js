import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';
import { getRelativeTimeString, getFormattedDate, addCommas } from '/js/user/individual/userProfileUtil.js';

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
	} else if (tabId === 'dashboard-tab') {
		loadDashboard();
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
	const response = await fetchWithAuth('/api/user/profile', { method: "GET" });
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

function loadDashboard() {
	document.getElementById('dashboard-container').innerHTML = '<div class="loading-message">대시보드 로딩 중</div>';

	const url = '/api/user/profile/' + userNo + '/dashboard';
	fetchWithAuth(url, { method: "GET" })
		.then(response => response.json())
		.then(data => {
			displayDashboard(data);
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

function displayDashboard(dashboardData) {
	const container = document.getElementById('dashboard-container');
	container.innerHTML = '';

	if (!dashboardData) {
		container.innerHTML = '<div class="empty-message">대시보드 정보를 불러올 수 없습니다.</div>';
		return;
	}

	const dashboardDiv = document.createElement('div');
	dashboardDiv.className = 'dashboard-content';

	const sellerData = dashboardData.sellerDashboardDTO;
	if (sellerData) {
		const sellerSection = document.createElement('div');
		sellerSection.className = 'dashboard-section seller-dashboard';

		const sellerTitle = document.createElement('h3');
		sellerTitle.textContent = '판매 활동 요약';
		sellerSection.appendChild(sellerTitle);

		const sellerLastUpdateInfo = createLastUpdateInfo(sellerData.sellerUpdatedDate);
		sellerSection.appendChild(sellerLastUpdateInfo);

		const sellerGrid = document.createElement('div');
		sellerGrid.appendChild(createDashboardItem('등급',
			sellerData.grade || '정보 없음', 'monthly-update-item'));
		sellerGrid.className = 'dashboard-grid';
		sellerGrid.appendChild(createDashboardItem('판매 중 상품',
			sellerData.activeItemCount !== null ? addCommas(sellerData.activeItemCount) + '개' : '0개'));
		sellerGrid.appendChild(createDashboardItem('누적 판매액',
			sellerData.totalSales !== null ? addCommas(Math.round(sellerData.totalSales)) + '원' : '0원'));

		sellerGrid.appendChild(document.createElement('br'));

		sellerGrid.appendChild(createDashboardItem('판매한 상품',
			sellerData.itemCount !== null ? addCommas(sellerData.itemCount) + '개' : '0개'));
		sellerGrid.appendChild(createDashboardItem('판매 완료',
			sellerData.completedSellItemCount !== null ? addCommas(sellerData.completedSellItemCount) + '건' : '0건'));
		sellerGrid.appendChild(createDashboardItem('판매 성공률',
			sellerData.salesSuccessRate !== null ? sellerData.salesSuccessRate.toFixed(1) + '%' : '0.0%'));
		sellerGrid.appendChild(createDashboardItem('판매 평점',
			sellerData.avgRating !== null ? sellerData.avgRating.toFixed(1) + '점' : '0.0점'));

		sellerSection.appendChild(sellerGrid);
		dashboardDiv.appendChild(sellerSection);
	} else {
		const noSellerInfo = document.createElement('p');
		noSellerInfo.textContent = '판매 활동 내역이 없습니다.';
		noSellerInfo.className = 'empty-message';
		dashboardDiv.appendChild(noSellerInfo);
	}

	const buyerData = dashboardData.buyerDashboardDTO;
	if (buyerData) {
		const buyerSection = document.createElement('div');
		buyerSection.className = 'dashboard-section buyer-dashboard';

		const buyerTitle = document.createElement('h3');
		buyerTitle.textContent = '구매 활동 요약';
		buyerSection.appendChild(buyerTitle);

		const buyerLastUpdateInfo = createLastUpdateInfo(buyerData.buyerUpdatedDate);
		buyerSection.appendChild(buyerLastUpdateInfo);

		const buyerGrid = document.createElement('div');
		buyerGrid.className = 'dashboard-grid';
		buyerGrid.appendChild(createDashboardItem('총 입찰 횟수',
			buyerData.bidCount !== null ? addCommas(buyerData.bidCount) + '회' : '0회'));
		buyerGrid.appendChild(createDashboardItem('입찰 중 상품',
			buyerData.activeBidItemCount !== null ? addCommas(buyerData.activeBidItemCount) + '개' : '0개'));
		buyerGrid.appendChild(createDashboardItem('입찰한 상품',
			buyerData.bidItemCount !== null ? addCommas(buyerData.bidItemCount) + '개' : '0개'));
		buyerGrid.appendChild(createDashboardItem('낙찰',
			buyerData.bidSuccessCount !== null ? addCommas(buyerData.bidSuccessCount) + '건' : '0건'));
		buyerGrid.appendChild(createDashboardItem('낙찰률',
			buyerData.bidSuccessRate !== null ? buyerData.bidSuccessRate.toFixed(1) + '%' : '0.0%'));

		buyerGrid.appendChild(document.createElement('br'));

		buyerGrid.appendChild(createDashboardItem('결제 완료',
			buyerData.paySuccessCount !== null ? addCommas(buyerData.paySuccessCount) + '건' : '0건'));
		buyerGrid.appendChild(createDashboardItem('결제율',
			buyerData.paySuccessRate !== null ? buyerData.paySuccessRate.toFixed(1) + '%' : '0.0%'));

		buyerSection.appendChild(buyerGrid);
		dashboardDiv.appendChild(buyerSection);
	} else {
		const noBuyerInfo = document.createElement('p');
		noBuyerInfo.textContent = '구매 활동 내역이 없습니다.';
		noBuyerInfo.className = 'empty-message';
		dashboardDiv.appendChild(noBuyerInfo);
	}

	container.appendChild(dashboardDiv);
}

function createDashboardItem(label, value, extraClass = '') {
	const itemDiv = document.createElement('div');
	itemDiv.className = `dashboard-item ${extraClass}`;

	const labelSpan = document.createElement('span');
	labelSpan.className = 'dashboard-item-label';
	labelSpan.textContent = label;

	const valueSpan = document.createElement('span');
	valueSpan.className = 'dashboard-item-value';
	valueSpan.textContent = value;

	itemDiv.appendChild(labelSpan);
	itemDiv.appendChild(valueSpan);
	return itemDiv;
}

function createLastUpdateInfo(updatedDate) {
	const sellerLastUpdateInfo = document.createElement('div');
	sellerLastUpdateInfo.className = 'last-update-info';

	const relativeTime = document.createElement('span');
	relativeTime.className = 'relative-time';
	relativeTime.textContent = getRelativeTimeString(updatedDate);
	sellerLastUpdateInfo.appendChild(relativeTime);

	const infoText = document.createElement('span');
	infoText.textContent = `(마지막 업데이트: ${getFormattedDate(updatedDate)})`;
	sellerLastUpdateInfo.appendChild(infoText);

	return sellerLastUpdateInfo;
}