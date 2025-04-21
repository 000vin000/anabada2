import { fetchWithoutRedirect } from '/js/user/common/fetchWithAuth.js';
import { checkIsOwnProfile, getRelativeTimeString, getFormattedDate, addCommas } from '/js/user/individual/userProfileUtil.js';

let sellPage = 0;
let buyPage = 0;
let size = 8;
let sellStatusFilter = 'all';
let buyStatusFilter = 'all';
let sellSort = 'recent';
let buySort = 'recent';

async function openTab(tabId) {
	document.querySelectorAll('.tab-content').forEach(tab => {
		tab.classList.remove('active');
	});

	document.querySelectorAll('.tab-button').forEach(button => {
		button.classList.remove('active');
	});

	document.getElementById(tabId).classList.add('active');
	document.querySelector('.tab-button[onclick="openTab(\'' + tabId + '\')"]').classList.add('active');

	if (tabId === 'sell-tab') {
		if (await checkIsOwnProfile(targetUserNo)) {
			loadPendingSellItems();
		}
		loadSellItems();
	} else if (tabId === 'buy-tab') {
		if (await checkIsOwnProfile(targetUserNo)) {
			loadPendingBuyItems();
		}
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
window.handleConfirmation = handleConfirmation;

document.addEventListener('DOMContentLoaded', async function() {
	const isOwnProfile = await checkIsOwnProfile(targetUserNo);
	console.log('본인프로필 여부: ' + isOwnProfile);

	if (isOwnProfile) {
		document.querySelectorAll('.logged-in-only').forEach(elem => {
			elem.hidden = false;
		});
	}

	openTab('sell-tab');
});

function loadSellItems() {
	document.getElementById('sell-items-container').innerHTML = '<div class="loading-message">판매 아이템 로딩 중</div>';

	const url = '/api/user/profile/' + targetUserNo + '/sells?page=' + sellPage + '&size=' + size
		+ '&status=' + sellStatusFilter + '&sort=' + sellSort;
	fetchWithoutRedirect(url, { method: "GET" })
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

	const url = '/api/user/profile/' + targetUserNo + '/buys?page=' + buyPage + '&size=' + size
		+ '&status=' + buyStatusFilter + '&sort=' + buySort;
	fetchWithoutRedirect(url, { method: "GET" })
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

function loadPendingSellItems() {
	const container = document.getElementById('sell-items-pending-container');
	if (container) {
		container.innerHTML = '<div class="loading-message">판매 확정 대기 목록 로딩 중...</div>';

		fetchWithoutRedirect(`/api/user/profile/${targetUserNo}/pending-sells`)
			.then(response => {
				if (!response.ok) {
					throw new Error(`HTTP error! status: ${response.status}`);
				}
				return response.json();
			})
			.then(data => {
				displayPendingItems(data, 'sell-items-pending-container', 'sell');
			})
			.catch(error => {
				console.error('Error loading pending sell items:', error);
				container.innerHTML = '<div class="error-message">판매 확정 대기 목록 로드 실패</div>';
			});
	}
}

function loadPendingBuyItems() {
	const container = document.getElementById('buy-items-pending-container');
	if (container) {
		container.innerHTML = '<div class="loading-message">구매 확정 대기 목록 로딩 중...</div>';

		fetchWithoutRedirect(`/api/user/profile/${targetUserNo}/pending-buys`)
			.then(response => {
				if (!response.ok) {
					throw new Error(`HTTP error! status: ${response.status}`);
				}
				return response.json();
			})
			.then(data => {
				displayPendingItems(data, 'buy-items-pending-container', 'buy');
			})
			.catch(error => {
				console.error('Error loading pending buy items:', error);
				container.innerHTML = '<div class="error-message">구매 확정 대기 목록 로드 실패</div>';
			});
	}
}

function loadDashboard() {
	document.getElementById('dashboard-container').innerHTML = '<div class="loading-message">대시보드 로딩 중</div>';

	const url = '/api/user/profile/' + targetUserNo + '/dashboard';
	fetchWithoutRedirect(url, { method: "GET" })
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

		if (item.reviewed) {
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

function displayPendingItems(items, containerId, type) {
	const container = document.getElementById(containerId);
	container.innerHTML = '';

	if (!items || items.length === 0) {
		container.innerHTML = `<div class="empty-message">
			${type === 'sell' ? '판매' : '구매'} 확정 대기 중인 아이템이 없습니다.</div>`;
		return;
	}

	const grid = document.createElement('div');
	grid.className = 'pending-items-grid';

	items.forEach(item => {
		const imageUrl = contextPath + '/image/' + item.itemNo;
		const detailUrl = contextPath + '/item/detail/' + item.itemNo;

		const link = document.createElement('a');
		link.href = detailUrl;
		link.className = 'item-card-link';

		const itemCard = document.createElement('div');
		itemCard.className = 'pending-item-card';
		itemCard.dataset.itemNo = item.itemNo;

		const img = document.createElement('img');
		img.src = imageUrl;
		img.alt = item.itemTitle;
		img.className = 'pending-item-image';
		img.onerror = () => {
			img.alt = '이미지가 없습니다';
		};

		const infoDiv = document.createElement('div');
		infoDiv.className = 'pending-item-info';

		const titleDiv = document.createElement('div');
		titleDiv.className = 'pending-item-title';
		titleDiv.textContent = item.itemTitle;

		const priceDiv = document.createElement('div');
		priceDiv.className = 'pending-item-price';
		priceDiv.textContent = `낙찰가: ${item.formattedFinalBidPrice || addCommas(item.itemFinalBidPrice || 0)}원`;

		const counterpartDiv = document.createElement('div');
		counterpartDiv.className = 'pending-item-counterpart';
		const counterpartBaseText = `${type === 'sell' ? '구매자' : '판매자'}: ${item.itemCounterNick || '정보 없음'}`;
		
		if (item.itemCounterUserNo && typeof contextPath !== 'undefined') {
			const profileLink = document.createElement('a');
			profileLink.href = `${contextPath}/user/profile/${item.itemCounterUserNo}`;
			profileLink.textContent = counterpartBaseText;
			counterpartDiv.appendChild(profileLink);
		} else {
			counterpartDiv.textContent = counterpartBaseText;
		}

		const deadlineDiv = document.createElement('div');
		deadlineDiv.className = 'pending-item-deadline';
		deadlineDiv.textContent = `확정 마감: ${item.formattedResvEndDate || '기한 없음'}`;

		const actionDiv = document.createElement('div');
		actionDiv.className = 'pending-item-action';

		const confirmButton = document.createElement('button');
		confirmButton.className = 'confirm-button';
		confirmButton.textContent = `${type === 'sell' ? '판매' : '구매'} 확정`;
		confirmButton.onclick = (event) => handleConfirmation(event, item.itemNo, type);

		actionDiv.appendChild(confirmButton);

		infoDiv.appendChild(titleDiv);
		infoDiv.appendChild(priceDiv);
		infoDiv.appendChild(counterpartDiv);
		infoDiv.appendChild(deadlineDiv);
		infoDiv.appendChild(actionDiv);

		itemCard.appendChild(img);
		itemCard.appendChild(infoDiv);

		link.appendChild(itemCard);
		grid.appendChild(link);
	});

	container.appendChild(grid);
}

async function handleConfirmation(event, itemNo, type) {
	event.stopPropagation();
	event.preventDefault();
	
	const actionText = type === 'sell' ? '판매' : '구매';
	if (!confirm(`${actionText} 확정하시겠습니까? 확정 후에는 거래 상태를 되돌릴 수 없습니다.`)) {
		return
	}

	const button = document.querySelector(`.pending-item-card[data-item-no="${itemNo}"] .confirm-button`);
	if (button) {
		button.disabled = true;
		button.textContent = '처리 중...';
	}

	const endpoint = type === 'sell'
		? `/api/item/detail/${itemNo}/sale-confirm`
		: `/api/item/detail/${itemNo}/purc-confirm`;

	try {
		const response = await fetchWithoutRedirect(endpoint, { method: 'PATCH' });

		if (response.ok) {
			alert(`${actionText} 확정이 완료되었습니다.`);

			const cardToRemove = document.querySelector(`.pending-item-card[data-item-no="${itemNo}"]`);
			if (cardToRemove) {
				const container = cardToRemove.parentNode.parentNode;
				cardToRemove.remove();

				if (container && !container.querySelector('.pending-item-card')) {
					container.innerHTML = `<div class="empty-message">${type === 'sell' ? '판매' : '구매'} 확정 대기 중인 아이템이 없습니다.</div>`;
				}
			}
		} else {
			const errorText = await response.text();
			console.error(`Confirmation failed for item ${itemNo}:`, response.status, errorText);
			alert(`${actionText} 확정 처리 중 오류가 발생했습니다. (${response.status})\n${errorText || '잠시 후 다시 시도해주세요.'}`);
			if (button) {
				button.disabled = false;
				button.textContent = `${actionText} 확정`;
			}
		}
	} catch (error) {
		console.error('Network or fetch error during confirmation:', error);
		alert(`${actionText} 확정 처리 중 네트워크 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.`);
		if (button) {
			button.disabled = false;
			button.textContent = `${actionText} 확정`;
		}
	}
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