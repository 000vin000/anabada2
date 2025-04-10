import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('noticeForm');
    
    if (!form) {
        console.error('Error: noticeForm not found in DOM');
        return;
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        // 폼 데이터 수집
        const formData = new FormData(form);
        const data = {
            noticeTitle: formData.get('noticeTitle'),
            noticeContent: formData.get('noticeContent')
        };

        // fetchWithAuth로 POST 요청 보내기
        fetchWithAuth('/api/notice/save', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: { 'Content-Type': 'application/json' }
        })
        .then(response => response.json())
        .then(result => {
            console.log(result);
            if (result.message) {
                alert(result.message || '공지사항이 등록되었습니다.');
                form.reset();
                window.location.href = '/admin/management';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버와의 통신 오류');
        });
    });
});
