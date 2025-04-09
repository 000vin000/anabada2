<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 등록</title>
</head>
<body>

    <h1>공지사항 등록</h1>

    <form id="noticeForm">
        <label for="noticeTitle">제목:</label>
        <input type="text" id="noticeTitle" name="noticeTitle" required><br>

        <label for="noticeContent">내용:</label><br>
        <textarea id="noticeContent" name="noticeContent" rows="5" cols="40" required></textarea><br>

        <input type="submit" value="등록">
        <a href="/admin/management"><button type="button">취소</button></a>
    </form>

    <!-- 스크립트는 HTML이 모두 로드된 후 실행되도록 body 끝에 배치 -->
    <script type="module">
        import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js'; // 실제 경로 확인 필요

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
                .then(response => response.json()) // 응답을 JSON으로 처리
                .then(result => {
                    console.log(result); // 서버 응답 확인
                    if (result.message) {
                        alert(result.message || '공지사항이 등록되었습니다.');
                        form.reset(); // 폼 초기화
                        window.location.href = '/admin/management';
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('서버와의 통신 오류');
                });
            });
        });
    </script>

</body>
</html>
