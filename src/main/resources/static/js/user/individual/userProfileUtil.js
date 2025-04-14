export function getRelativeTimeString(dateString) {
	const date = new Date(dateString);
	const now = new Date();
	const diffInHours = Math.floor((now - date) / (1000 * 60 * 60));

	if (diffInHours < 24) {
		return '오늘 업데이트됨';
	} else if (diffInHours < 48) {
		return '어제 업데이트됨';
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