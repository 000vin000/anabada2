<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>공지사항 수정</title>
</head>
<body>
    <div class="container">
        <h2>공지사항 수정</h2>

        <!-- 수정 폼 -->
        <form id="editNoticeForm" method="post">
            <div class="form-group">
                <label for="noticeTitle">제목</label>
                <input type="text" id="noticeTitle" name="noticeTitle" value="${notice.noticeTitle}" required>
            </div>

            <div class="form-group">
                <label for="noticeContent">내용</label>
                <textarea id="noticeContent" name="noticeContent" required>${notice.noticeContent}</textarea>
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary">수정하기</button>
                <a href="${pageContext.request.contextPath}/admin/management" class="btn btn-secondary">취소</a>
            </div>
            <input type="hidden" id="noticeNo" name="noticeNo" value="${notice.noticeNo}" />
        </form>
    </div>

    <script type="module">
        import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';  // JWT 포함 요청 함수

        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('editNoticeForm');
            
            form.addEventListener('submit', function(event) {
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
                    method: 'PUT',  // PUT 요청으로 수정
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(updatedNotice),  // 수정된 공지사항 내용
                })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else if (response.status === 401) {
                        alert('로그인이 필요합니다. 토큰이 만료되었을 수 있습니다.');
                        window.location.href = '/login';  // 로그인 페이지로 리다이렉트
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
                        alert(data.message);  // 서버에서 반환한 메시지 출력
                        window.location.href = '/admin/management';  // 수정 후 목록 페이지로 리다이렉트
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('수정 중 오류가 발생했습니다.');
                });
            });
        });
    </script>

</body>
</html>
