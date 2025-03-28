<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ANABADA</title>
	<link rel="stylesheet" type="text/css" href="/css/styleTemp.css">
	<script type="module" src="/js/user/fetchWithAuth.js"></script> 
</head>
<body>
<div class="body-container">
	
	<div>
		<jsp:include page="../mypage/coinView.jsp" />
	</div>
	
	<!-- 사용자 정보 -->
	<section class="user-info">
		<h3>사용자 정보</h3>
		<a href="#" onclick="goToUpdateInfo()">회원정보 수정</a>
		<div id="user-info"></div>
	</section>
	<div>
		주문내역
		취소 반품 교환 내역
		재입고 알림 내역
		최근 본 상품
		나의 브랜드 리스트
		나의 맞춤 정보
	</div>
	<div>
		참여 혜택
		리서치 참여
		체험단 신청/응모 내역
		래플 응모 내역
		크리에이터 마켓플레이스
		무신사 큐레이터 서비스
	</div>
	<div>
		무신사 페이 관리
		무신사 현대카드 할인정보
	</div>
	<div>
		고객센터
		1:1 문의 내역
		상품 문의 내역
		공지 사항
	</div>

<!-- J회원정보 수정 -->
<script type="module">
  import { goToUpdateInfo } from '/js/user/goToUpdateInfo.js';  // ✅ 리팩토링된 파일명 반영
  window.goToUpdateInfo = goToUpdateInfo;
</script>
<script type="module">
document.addEventListener("DOMContentLoaded", async function () {
    try {
        const response = await fetchWithAuth("/api/coin", { method: "GET" });

        if (response.ok) {
            const { goods } = await response.json();
            document.getElementById("user-info").innerHTML = `
                <p>👤 ${goods.userNo}</p>
                <p>회원번호: ${goods.userNo}</p>
                <p>보유 코인: ${goods.goodsCash} 코인</p>
            `;
        } else {
            document.getElementById("user-info").innerHTML = `<p>사용자 정보를 불러올 수 없습니다.</p>`;
        }
    } catch (error) {
        console.error("마이페이지 정보 로드 실패:", error);
        document.getElementById("user-info").innerHTML = `<p>오류 발생</p>`;
    }
});

</script>
<jsp:include page="../footer.jsp" />
<jsp:include page="../main/bottom_nav.jsp" />
</body>
</html>
