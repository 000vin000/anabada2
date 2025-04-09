import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';
import { getVerifiedLoggedInUserNo, addCommas, formatTimeText } from '/js/item/itemDetailUtil.js';

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
let intervals = [];
let status = '';

function changeMainImage(src) {
	if (mainImage) {
		mainImage.src = src;
	}
}
window.changeMainImage = changeMainImage;

document.addEventListener('DOMContentLoaded', async function() {
	status = initialItemStatus;

	if ((status === '대기중' || status === '판매중')
		&& initialItemSaleEndDate !== 'null') {

		startInterval(() => updateStatus(itemNo), 5000);
		let inner = await updateRemainTime(itemNo);
		await inner();
		startInterval(inner, 1000);
		startInterval(() => updatePrice(itemNo), 5000);
	} else {
		if (priceInputSection) priceInputSection.hidden = true;
		if (timeSection) timeSection.hidden = true;
	}

	await verifyLoggedInAndInit();
});

async function verifyLoggedInAndInit() {
	loggedInUserNo = await getVerifiedLoggedInUserNo();
	isOwnItem = loggedInUserNo !== 0 && loggedInUserNo === sellerNo;
	console.log('로그인 uid: ' + loggedInUserNo);
	console.log('본인아이템 여부: ' + isOwnItem);

	if (isOwnItem) {
		document.querySelectorAll('.logged-in-only').forEach(elem => {
			elem.hidden = false;
		});
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

				//fetchWithAuth() then으로도 이어지는지 확인 필요
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
	if (!priceElement) {
		return;
	}
	
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

async function updateStatus(itemNo) {
	try {
		const response = await fetch(`/api/item/detail/${itemNo}/status`);
		const data = await response.text();

		if (data !== status) {
			status = data;

			if (statusElement) statusElement.innerText = status;
			if (priceHeadingElement) priceHeadingElement.innerText = status;

			if (status !== '대기중' && status !== '판매중') {
				stopAllIntervals();
				if (priceInputSection) priceInputSection.hidden = true;
				if (timeSection) timeSection.hidden = true;

			} else if (status === '판매중') {
				if (priceInputSection) priceInputSection.hidden = false;
				if (priceText) priceText.disabled = isOwnItem || loggedInUserNo === 0;
				if (bidBtn) bidBtn.disabled = isOwnItem || loggedInUserNo === 0;

			} else {
				if (priceInputSection) priceInputSection.hidden = true;
			}
		}
	} catch (error) {
		console.error('updateStatus() error: ', error.message);
	}
}

async function getRemainTime(itemNo) {
	try {
		const response = await fetch(`/api/item/detail/${itemNo}/remainTime`);
		const data = await response.json();
		const { remainTime, type } = data;

		if (remainTimeHeading) {
			remainTimeHeading.innerText = '경매 ' + type + '까지 남은 시간';
		}
		return { remainTime, type };

	} catch (error) {
		console.error('getRemainTime() error: ', error.message);
		if (remainTimeHeading) remainTimeHeading.innerText = '시간 로딩 실패';
		return { remainTime: 0, type: '오류' };
	}
}

async function updateRemainTime(itemNo) {
	let response = await getRemainTime(itemNo);
	let { remainTime } = response;
	const { type } = response;

	let inner = async function() {
		if (remainTime <= 0) {
			if (type === '종료') {
				return;

			} else if (type === '시작') {
				if (await waitForStatus(itemNo, '판매중')) {
					let newTimeData = await getRemainTime(itemNo);
					remainTime = newTimeData.remainTime;
					if (priceInputSection) priceInputSection.hidden = false;

				} else {
					stopAllIntervals();
					if (remainTimeElement) remainTimeElement.innerText = '상태 변경 실패';
					return;
				}
			}
		}

		if (remainTimeElement && remainTime > 0) {
			document.getElementById('remain-time').innerText = formatTimeText(remainTime);
		} else if (remainTimeElement && type === '종료') {
			remainTimeElement.innerText = '경매 종료';
		}
		if (remainTime > 0) {
			remainTime--;
		}
	};

	return inner;
}

async function waitForStatus(itemNo, targetStatus, maxAttempts = 10, interval = 500) {
	await updateStatus(itemNo);
	if (status === targetStatus) {
		return true;
	}

	for (let attempt = 1; attempt <= maxAttempts; attempt++) {
		await new Promise(resolve => setTimeout(resolve, interval));
		await updateStatus(itemNo);

		if (status === targetStatus) {
			return true;
		}
		if (status !== '대기중' && status !== '판매중') {
			return false;
		}
	}

	console.log(`${maxAttempts}회 시도 후 실패: '${targetStatus}' 상태`);
	return false;
}