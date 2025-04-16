import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';
import { getVerifiedLoggedInUserNo, addCommas, formatTimeText } from '/js/item/itemDetailUtil.js';
import { initChatRoomList } from '/js/chat/chatRoomList.js';
import { initInquiryButton } from '/js/chat/ItemDetail.js';

const priceInputSection = document.getElementById('price-input-section');
const timeSection = document.getElementById('time-section');
const bidBtn = document.getElementById('bid-btn');
const mainImage = document.getElementById('main-image');
const remainTimeHeading = document.getElementById('remain-time-heading');
const remainTimeElement = document.getElementById('remain-time');
const statusElement = document.getElementById('status');
const priceHeadingElement = document.getElementById('price-heading');
const priceElement = document.getElementById('price');
const priceText = document.getElementById('price-text');

let loggedInUserNo = 0;
let isOwnItem = false;

let saleStartTime = null;
let saleEndTime = null;
let currentClientStatus;
let intervals = [];

function changeMainImage(src) {
	if (mainImage) {
		mainImage.src = src;
	}
}
window.changeMainImage = changeMainImage;

document.addEventListener('DOMContentLoaded', async function() {
	currentClientStatus = initialItemStatus;
	
	await initTimeInfo(itemNo);
	await verifyLoggedInAndInit();
});

export async function verifyLoggedInAndInit() {
	
	loggedInUserNo = await getVerifiedLoggedInUserNo();
	isOwnItem = loggedInUserNo !== 0 && loggedInUserNo === sellerNo;

	console.log('로그인된 userNo:', loggedInUserNo);
	console.log('sellerNo:', sellerNo);
	console.log('본인 아이템 여부:', isOwnItem);

	// 버튼 노출 제어
	const chatBtn = document.querySelector(".viewChatRoomsBtn");
	const inquiryBtn = document.getElementById("inquiryBtn");

	if (isOwnItem) {
		if (chatBtn) chatBtn.style.display = "inline-block";
		initChatRoomList();

		document.querySelectorAll('.logged-in-only').forEach(elem => {
			elem.hidden = false;
		});
	} else {
		if (inquiryBtn) inquiryBtn.style.display = "inline-block";
		initInquiryButton();
	}
}

function startInterval(f, s) {
	let interval = setInterval(f, s);
	intervals.push(interval);
}

function stopAllIntervals() {
	intervals.forEach(id => {
		clearInterval(id);
	});
	intervals = [];
}

if (priceText) {
	priceText.addEventListener('keypress', function(e) {
		if (!/^\d$/.test(e.key) && !['Backspace', 'Tab', 'Delete', 'ArrowLeft', 'ArrowRight'].includes(e.key)) {
			e.preventDefault();
		}
	});

	priceText.addEventListener('input', function() {
		this.value = this.value.replace(/[^\d]/g, '');
	});

	if (bidBtn) {
		bidBtn.addEventListener('click', async function() {
			try {
				if (loggedInUserNo === 0 || loggedInUserNo === null) {
					Swal.fire({
						title: '로그인 필요',
						html: '로그인이 필요한 서비스입니다.<br>로그인 페이지로 이동하시겠습니까?',
						icon: 'warning',
						showCancelButton: true,
						confirmButtonText: '로그인',
						cancelButtonText: '취소'
					}).then((result) => {
						if (result.isConfirmed) {
							window.location.href = '/'; // login url로 교체
						}
					});
					throw new Error('로그인 인증 오류');
				}

				const newPriceValue = priceText.value.trim();
				if (!newPriceValue || isNaN(Number(newPriceValue))) {
					Swal.fire('입력 오류', '유효한 입찰 금액을 입력하세요.', 'warning');
					throw new Error('입찰가 입력 변환 오류');
				}
				const newPrice = Number(newPriceValue);
				
				const currentPriceText = priceElement.innerText.replace(/[^0-9]/g, '');
				const currentPrice = Number(currentPriceText);

				if (newPrice < currentPrice + 1000) {
					Swal.fire('입찰 실패', '입찰가는 현재가보다 1,000원 이상 높아야 합니다.', 'error');
					throw new Error('입찰가 1000+ 오류');
				}
				if (isOwnItem) {
					Swal.fire('입찰 실패', '자신의 물품에는 입찰할 수 없습니다.', 'error');
					throw new Error('자신 물품 입찰 오류');
				}

				const balanceData = await fetchWithAuth(`/api/item/detail/user/balance`, { method: 'GET' })
					.then(response => {
						if (!response.ok) {
							return response.json()
								.then(errorObj => {
									throw new Error(errorObj?.message || `잔액 조회 실패: ${response.status}`);
								});
						}
						return response.text();
					});
					const fetchedBalance = parseFloat(balanceData);

					if (isNaN(fetchedBalance)) {
						throw new Error('유효하지 않은 잔액 정보');
					}

					const userBalance = fetchedBalance;
					console.log(`${loggedInUserNo} 유저의 보유 코인: ${userBalance}`);

					if (userBalance < newPrice) {
						Swal.fire('입찰 실패', '코인이 부족합니다.', 'error');
						throw new Error('코인 부족');
					}
					
					await Swal.fire({
						title: '입찰 확인',
						html: '<p>현재 포인트 잔액은 <b>' + addCommas(userBalance) + '원</b>입니다.</p>'
							+ '<p><b>' + addCommas(newPrice) + '원</b>으로 입찰하시겠습니까?</p>',
						icon: 'question',
						showCancelButton: true,
						confirmButtonText: '입찰',
						cancelButtonText: '취소'
					})
					.then((result) => {
						if (result.isConfirmed) {
							fetchWithAuth(`/api/item/detail/${itemNo}/bid`, {
								method: 'PATCH',
								body: JSON.stringify({ newPrice: newPrice })
							})
							.then(response => {
								if (!response.ok) {
									return response.json()
										.then(errorObj => {
											throw new Error(errorObj?.message || `입찰 요청 실패: ${response.status}`);
										});
								}
								return response.text();
							})
							.then(data => {
								Swal.fire('입찰 완료', data, 'success');
							})
						}
					});
			} catch (error) {
				console.error('입찰 프로세스 중 오류 발생:', error);
			}
		});
	}
}

function updatePrice(itemNo) {
	if (priceElement) {
		fetch(`/api/item/detail/${itemNo}/price`)
			.then(response => {
				if (!response.ok) {
					throw new Error(`현재가 갱신 실패: ${response.status}`);
				}
				return response.text();
			})
			.then(newPriceText => {
				const currentDisplayedText = priceElement.innerText;
				const currentDisplayedPrice = currentDisplayedText.replace(/[^0-9]/g, '');
				const newPrice = newPriceText.trim().replace(/\.\d*$/, '');;

				if (newPrice !== currentDisplayedPrice) {
					priceElement.innerText = addCommas(newPrice) + ' 원';
					priceText.value = parseInt(newPrice, 10) + 1000;
				}
			})
			.catch(error => {
				console.error('updatePrice() error: ', error.message);
			});
	}
}

async function initTimeInfo(itemNo) {
	try {
		const response = await fetch(`/api/item/detail/${itemNo}/time-info`);
		const timeData = await response.json();
		saleStartTime = new Date(timeData.saleStartTime).getTime();
		saleEndTime = new Date(timeData.saleEndTime).getTime();
		currentClientStatus = timeData.status;

		updateDisplay();
		
		if (currentClientStatus === 'WAITING' || currentClientStatus === 'ACTIVE') {
			startInterval(() => syncStatusFromServer(itemNo), 1000);
			startInterval(updateDisplay, 1000);
			startInterval(() => updatePrice(itemNo), 5000);

			if (timeSection) timeSection.hidden = false;
			if (priceInputSection) priceInputSection.hidden = false;
		} else {
			if (timeSection) timeSection.hidden = true;
			if (priceInputSection) priceInputSection.hidden = true;
		}
	} catch (error) {
		console.error('타이머 초기화 실패:', error);
	}
}

function updateDisplay() {
	const now = Date.now();
	let remainingSeconds = 0;
	let displayType = '';

	if (currentClientStatus === 'WAITING') {
		remainingSeconds = Math.max(0, Math.floor((saleStartTime - now) / 1000));
		displayType = '시작';
	} else if (currentClientStatus === 'ACTIVE') {
		remainingSeconds = Math.max(0, Math.floor((saleEndTime - now) / 1000));
		displayType = '종료';
	} else {
		stopAllIntervals();
		if (timeSection) timeSection.hidden = true;
		if (priceInputSection) priceInputSection.hidden = true;
		return;
	}

	if (remainTimeHeading) {
		remainTimeHeading.innerText = '경매 ' + displayType + '까지 남은 시간';
	}
	if (remainTimeElement && remainingSeconds >= 0) {
		remainTimeElement.innerText = formatTimeText(remainingSeconds);
	}
}

async function syncStatusFromServer(itemNo) {
	try {
		const response = await fetch(`/api/item/detail/${itemNo}/status`);
		const serverStatus = await response.text();

		if (serverStatus !== currentClientStatus) {
			console.log(`상태 변경 감지: ${currentClientStatus} -> ${serverStatus}`);
			currentClientStatus = serverStatus;
			updateStatusUI(currentClientStatus);
		}
	} catch (error) {
		console.error('서버 상태 동기화 오류:', error);
	}
}

function updateStatusUI(status) {
	console.log('updateStatusUI 호출됨. 전달받은 status:', typeof status, status);
	    console.log('비교 대상 currentClientStatus:', typeof currentClientStatus, currentClientStatus);
	
	if (statusElement) statusElement.innerText = status;

	if (currentClientStatus !== 'WAITING' && currentClientStatus !== 'ACTIVE') {
		stopAllIntervals();
		if (timeSection) timeSection.hidden = true;
		if (priceInputSection) priceInputSection.hidden = true;
	}
}