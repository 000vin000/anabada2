import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';

export async function getVerifiedLoggedInUserNo() {
	try {
		const response = await fetchWithAuth('/api/item/detail', { method: 'GET' });

		if (response.ok) {
			const userNoText = await response.text();
			return parseInt(userNoText) || 0;
		}
	} catch (error) {
		console.error('로그인인증 에러:', error);
	}
	return 0;
}

export function addCommas(num) {
	if (num === null || num === undefined || isNaN(Number(num))) {
		return num;
	}

	const numStr = Number(num).toString();
	return numStr.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

export function formatTimeText(totalSeconds) {
	if (isNaN(totalSeconds) || totalSeconds < 0) {
		return '계산 중';
	}

	const days = Math.floor(totalSeconds / 86400);
	const hours = Math.floor((totalSeconds % 86400) / 3600);
	const minutes = Math.floor((totalSeconds % 3600) / 60);
	const seconds = totalSeconds % 60;

	let timeText = '';
	if (days > 0) timeText += days + '일 ';
	if (hours > 0) timeText += hours + '시간 ';
	if (minutes > 0) timeText += minutes + '분 ';
	if (seconds > 0) timeText += seconds + '초';

	return timeText.trim();
}

export function openWindow(name, url, width = 650, height = 800) {
	window.open(
		url, name, `width=${width},height=${height},scrollbars=yes`
	);
}