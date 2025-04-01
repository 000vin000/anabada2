<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고객센터 문의 작성</title>
<script type="module">
    // fetchWithAuth.js에서 fetchWithAuth 함수 가져오기
    import { fetchWithAuth } from '/js/user/fetchWithAuth.js';  // 파일 경로는 실제 파일 위치에 맞게 설정
    
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('questionForm');
        
        form.addEventListener('submit', function(event) {
            event.preventDefault(); // 기본 폼 제출 방지

            // 폼 데이터 수집
            const formData = new FormData(form);
            const data = {
                questionTitle: formData.get('questionTitle'),
                questionContent: formData.get('questionContent'),
                qIsPrivate: formData.has('qIsPrivate') ? 'true' : 'false'  // 체크박스 여부 처리
            };

            // fetchWithAuth로 POST 요청 보내기
            fetchWithAuth('/api/question/write', {
                method: 'POST',
                body: JSON.stringify(data), // JSON 형식으로 데이터를 전송
            })
            .then(response => response.json()) // 응답을 JSON으로 처리
            .then(result => {
                console.log(result); // 서버에서 받은 응답 확인
                if (result.message) {
                    alert(result.message || '문의 작성 완료');
                    form.reset();  // 폼 초기화

                    // 문의 작성 후 myQuestions 페이지로 리다이렉트
                    window.location.href = '/question/mypage/myQuestions'; // 이 경로가 정확한지 확인
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버와의 통신 오류');
            });
        });
    });
</script>
</head>
<body>
<h2>고객센터 문의 작성</h2>
<form id="questionForm">
    <label for="questionTitle">제목:</label>
    <input type="text" id="questionTitle" name="questionTitle" required /><br>

    <label for="questionContent">내용:</label><br>
    <textarea id="questionContent" name="questionContent" rows="5" cols="40" required></textarea><br>

    <label for="qIsPrivate">비밀글 여부:</label>
    <input type="checkbox" id="qIsPrivate" name="qIsPrivate" value="true" /><br>

    <input type="submit" value="문의하기" />
</form>
</body>
</html>
