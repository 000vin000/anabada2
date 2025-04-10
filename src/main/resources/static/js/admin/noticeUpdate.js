import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';  // JWT 포함 요청 함수

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('editNoticeForm');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const noticeNo = document.getElementById('noticeNo').value;
        const noticeTitle = document.getElementById('noticeTitle').value.trim();
        const noticeContent = document.getElementById('noticeContent').value.trim();

        if (!noticeTitle || !noticeContent) {
            alert("제목과 내용을 입력해주세요.");
            return;
        }

        const updatedNotice = {
            noticeNo: noticeNo,
            noticeTitle: noticeTitle,
            noticeContent: noticeContent
        };

        // PUT 요청을 통해 수정 요청
        fetchWithAuth(`/api/notice/update/${noticeNo}`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedNotice),
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 401) {
                alert('로그인이 필요합니다. 토큰이 만료되었을 수 있습니다.');
                window.location.href = '/login';
                throw new Error('Unauthorized');
            } else if (response.status === 403) {
                alert('수정 권한이 없습니다.');
                throw new Error('Forbidden');
            } else {
                alert('수정 실패. 서버에서 오류가 발생했습니다. 다시 시도해 주세요.');
                return response.json();
            }
        })
        .then(data => {
            if (data && data.message) {
                alert(data.message);
                window.location.href = '/admin/management';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('수정 중 오류가 발생했습니다.');
        });
    });
});
