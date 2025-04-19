import { fetchWithoutRedirect } from '/js/user/common/fetchWithAuth.js';

export async function checkIsOwnProfile(targetUserNo) {
	try {
		const response = await fetchWithoutRedirect('/api/user/profile', { method: "GET" });
		if (response.ok) {
			const loggedInUserNoText = await response.text();
			const loggedInUserNo = parseInt(loggedInUserNoText.trim(), 10);
			return !isNaN(loggedInUserNo) && loggedInUserNo > 0 && loggedInUserNo === targetUserNo;
		} else {
			console.warn(`로그인 상태 확인 실패: ${response.status}`);
			return false;
		}
	} catch (error) {
		console.error("로그인 상태 확인 중 네트워크 오류:", error);
		return false;
	}
}

export function getRelativeTimeString(dateString) {
	const date = new Date(dateString);
	const now = new Date();
	const diffInHours = Math.floor((now - date) / (1000 * 60 * 60));

	if (diffInHours < 24) {
		return '24시간 이내 업데이트됨';
	} else {
		return `${Math.floor(diffInHours / 24)}일 전 업데이트됨`;
	}
}

export function getFormattedDate(dateString) {
	const date = new Date(dateString);
	return date.toLocaleDateString('ko-KR', {
		year: 'numeric',
		month: 'long',
		day: 'numeric'
	});
}

export function addCommas(num) {
	if (num === null || num === undefined || isNaN(Number(num))) {
		return num;
	}

	const numStr = Number(num).toString();
	return numStr.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}