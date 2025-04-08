<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="/images/favicon.png" sizes="16x16">
    <title>ANABADA</title>
    <script defer src="/js/header.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/styleCategoryModal.css">
</head>

<body>
    <header>
        <a href="/"><img src="/images/logo.png" alt="로고" /></a>

		<!-- 카테고리 모달 열기 버튼 -->
		<div id="imgCategoryModal">
			<button style="border: none;"><img alt="category" src="/images/icon/category.png" style="width: 35px; height: 35px; cursor: pointer;" onclick="openCategoryModal()"></button>
		</div>

        <!-- 검색 -->
        <div id="findOption">
            <select id="findType" name="findType">
                <option value="" selected disabled>검색유형</option>
                <option value="itemName">상품명</option>
                <option value="userNick">닉네임(동일)</option> 
            </select>
            <input type="search" id="keyword" name="keyword" placeholder="검색어를 입력하세요" onkeydown="handleKeyPress(event)">
            <input type="button" id="find" value="검색" onclick="submitSearch()">
        </div>

        <nav>
            <ul>
                <li><a id="loginBtn" href="/auth/login/individual/IndividualLogin.html">로그인</a></li>
                <li><a id="joinBtn" href="/auth/join/individual/IndividualJoin.html">회원가입</a></li>
                <li><a id="mypageBtn" href="/mypage" style="display: none;">마이페이지</a></li>
                <li><a id="logoutBtn" href="#" style="display: none;">로그아웃</a></li>
            </ul>
        </nav>   

        <!-- 로그인된 사용자 -->
        <div id="itemUpload" style="display: none;">
            <a href="/item/mypage/itemup">상품등록</a>
        </div>
    </header>

    <!-- 카테고리 모달 -->
    <div id="categoryModal" class="modal" style="display: none;">
    	<div class="modal-content">
    		<h2>카테고리 선택</h2>
    		<div id="categoryModalLevel1">
    			<p class="genderOption" id="genderAll" data-value="00">전체</p>
    			<p class="genderOption" id="genderMale" data-value="10">남성</p>
    			<p class="genderOption" id="genderFemale" data-value="20">여성</p>
    		</div>
    		<div id="categoryModalBody">
				<div id="categoryModalLevel2">
	    			<!-- 내용 -->
	    		</div>
				<div id="categoryModalLevel3">
	    			<!-- 내용 -->
	    		</div>
    		</div>
    	</div>
    </div>
    <script type="module" src="/js/user/common/authCheck.js"></script>
    <script type="module" src="/js/user/common/logout.js"></script>
</body>
<script>
document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("categoryModal");
    const genderOptions = document.querySelectorAll(".genderOption");
    const level2Container = document.getElementById("categoryModalLevel2");
    const level3Container = document.getElementById("categoryModalLevel3");

    document.getElementById("imgCategoryModal").addEventListener("click", openCategoryModal);

    function openCategoryModal() {
        modal.style.display = "block";
    }

    function closeCategoryModal() {
        modal.style.display = "none";
    }

    genderOptions.forEach(p => {
        p.addEventListener("click", function () {
			// 선택된 genderOption 추출
			const level1 = this.getAttribute("data-value");
			let level2msg = "";
			
			switch (level1) {
				case "00": 
					level2msg = `
						<button class="hct" data-value="01">아우터</button>
						<button class="hct" data-value="02">상의</button>
            			<button class="hct" data-value="03">하의</button>
            			<button class="hct" data-value="04">원피스</button>
            			<button class="hct" data-value="05">스커트</button>
            			<button class="hct" data-value="06">가방</button>
            			<button class="hct" data-value="07">패션소품</button>
            			<button class="hct" data-value="08">신발</button>
					`;
					break;
				case "10":
					level2msg = `
	                    <button class="hct" data-value="01">아우터</button>
	                    <button class="hct" data-value="02">상의</button>
	                    <button class="hct" data-value="03">하의</button>
	                    <button class="hct" data-value="06">가방</button>
	                    <button class="hct" data-value="07">패션소품</button>
	                    <button class="hct" data-value="08">신발</button>
	                `;
	                break;
	            case "20":
	            	level2msg = `
	                    <button class="hct" data-value="01">아우터</button>
	                    <button class="hct" data-value="02">상의</button>
	                    <button class="hct" data-value="03">하의</button>
	                    <button class="hct" data-value="04">원피스</button>
	                    <button class="hct" data-value="05">스커트</button>
	                    <button class="hct" data-value="06">가방</button>
	                    <button class="hct" data-value="07">패션소품</button>
	                    <button class="hct" data-value="08">신발</button>
	                `;
	                break;
			}
			document.getElementById("categoryModalLevel2").innerHTML = level2msg;
			
			document.querySelectorAll(".genderOption").forEach(btn => btn.classList.remove("hActive"));
			this.classList.add("hActive");
        });
    });

    level2Container.addEventListener("click", function (event) {
        if (event.target.classList.contains("hct")) {
        	document.querySelectorAll(".hct").forEach(btn => btn.classList.remove("hActive"));
			event.target.classList.add("hActive");
			
			const level2 = event.target.getAttribute("data-value");
			let level3msg = "";
			
			// level3 버튼 업데이트
			switch (level2) {
			case "01":
				level3msg = `
					<button class="hcd" data-value="sel00">전체</button>
                    <button class="hcd" data-value="sel01">블루종</button>
                    <button class="hcd" data-value="sel02">레더 재킷</button>
                    <button class="hcd" data-value="sel03">카디건</button>
                    <button class="hcd" data-value="sel04">트러커 재킷</button>
                    <button class="hcd" data-value="sel05">블레이저</button>
                    <button class="hcd" data-value="sel06">스타디움</button>
                    <button class="hcd" data-value="sel07">나일론/아노락 재킷</button>
                    <button class="hcd" data-value="sel08">트레이닝</button>
                    <button class="hcd" data-value="sel09">사파리/헌팅 재킷</button>
                    <button class="hcd" data-value="sel10">베스트/패딩베스트</button>
                    <button class="hcd" data-value="sel11">숏패딩/숏헤비</button>
                    <button class="hcd" data-value="sel12">퍼/무스탕/플리스</button>
                    <button class="hcd" data-value="sel13">롱패딩/롱헤비</button>
                    <button class="hcd" data-value="sel14">숏코트</button>
                    <button class="hcd" data-value="sel15">롱코트</button>
                    <button class="hcd" data-value="sel16">트렌치코트</button>
                    <button class="hcd" data-value="sel17">기타 아우터</button>
				`;
				break;
			case "02":
				level3msg = `
                    <button class="hcd" data-value="sel00">전체</button>
                    <button class="hcd" data-value="sel01">맨투맨</button>
                    <button class="hcd" data-value="sel02">후드</button>
                    <button class="hcd" data-value="sel03">셔츠/블라우스</button>
                    <button class="hcd" data-value="sel04">긴소매 티셔츠</button>
                    <button class="hcd" data-value="sel05">반소매 티셔츠</button>
                    <button class="hcd" data-value="sel06">카라 티셔츠</button>
                    <button class="hcd" data-value="sel07">니트</button>
                    <button class="hcd" data-value="sel08">민소매 티셔츠</button>
                    <button class="hcd" data-value="sel09">기타 상의</button>
				`;
				break;
			case "03":
				level3msg = `
                    <button class="hcd" data-value="sel00">전체</button>
                    <button class="hcd" data-value="sel01">데님</button>
                    <button class="hcd" data-value="sel02">트레이닝</button>
                    <button class="hcd" data-value="sel03">코튼</button>
                    <button class="hcd" data-value="sel04">슬랙스</button>
                    <button class="hcd" data-value="sel05">숏</button>
                    <button class="hcd" data-value="sel06">레깅스</button>
                    <button class="hcd" data-value="sel07">점프슈트</button>
                    <button class="hcd" data-value="sel08">기타 하의</button>
				`;
				break;
			case "04":
				level3msg = `
                    <button class="hcd" data-value="sel00">전체</button>
                    <button class="hcd" data-value="sel01">미니</button>
                    <button class="hcd" data-value="sel02">미디</button>
                    <button class="hcd" data-value="sel03">맥시</button>
                    <button class="hcd" data-value="sel04">기타 원피스</button>
				`;
				break;
			case "05":
				level3msg = `
                    <button class="hcd" data-value="sel00">전체</button>
                    <button class="hcd" data-value="sel01">미니</button>
                    <button class="hcd" data-value="sel02">미디</button>
                    <button class="hcd" data-value="sel03">맥시</button>
                    <button class="hcd" data-value="sel04">기타 스커트</button>
				`;
				break;
			case "06":
				level3msg = `
                    <button class="hcd" data-value="sel00">전체</button>
                    <button class="hcd" data-value="sel01">메신저백</button>
                    <button class="hcd" data-value="sel02">크로스백</button>
                    <button class="hcd" data-value="sel03">숄더백</button>
                    <button class="hcd" data-value="sel04">백팩</button>
                    <button class="hcd" data-value="sel05">토트백</button>
                    <button class="hcd" data-value="sel06">에코백</button>
                    <button class="hcd" data-value="sel07">보스턴백</button>
                    <button class="hcd" data-value="sel08">파우치</button>
                    <button class="hcd" data-value="sel09">캐리어</button>
                    <button class="hcd" data-value="sel10">지갑</button>
                    <button class="hcd" data-value="sel11">클러치백</button>
                    <button class="hcd" data-value="sel12">기타 가방</button>
				`;
				break;
			case "07":
				level3msg = `
                    <button class="hcd" data-value="sel00">전체</button>
                    <button class="hcd" data-value="sel01">모자</button>
                    <button class="hcd" data-value="sel02">머플러</button>
                    <button class="hcd" data-value="sel03">주얼리</button>
                    <button class="hcd" data-value="sel04">양말</button>
                    <button class="hcd" data-value="sel05">아이웨어</button>
                    <button class="hcd" data-value="sel06">시계</button>
                    <button class="hcd" data-value="sel07">벨트</button>
                    <button class="hcd" data-value="sel08">기타 액세서리</button>
				`;
				break;
			case "08":
				level3msg = `
                    <button class="hcd" data-value="sel00">전체</button>
                    <button class="hcd" data-value="sel01">스니커즈</button>
                    <button class="hcd" data-value="sel02">슬리퍼</button>
                    <button class="hcd" data-value="sel03">부츠</button>
                    <button class="hcd" data-value="sel04">구두</button>
                    <button class="hcd" data-value="sel05">로퍼</button>
                    <button class="hcd" data-value="sel06">운동화</button>
                    <button class="hcd" data-value="sel07">기타 신발</button>
				`;
				break;
			}
			document.getElementById("categoryModalLevel3").innerHTML = level3msg;
			
			document.querySelectorAll(".hcd").forEach(btn => btn.classList.remove("hActive"));
			event.target.classList.add("hActive");
        }
    });

    level3Container.addEventListener("click", function (event) {
        if (event.target.classList.contains("hcd")) {
        	const hGender = document.querySelector(".genderOption.hActive")?.getAttribute("data-value") || "";
			const hClothesType = document.querySelector(".hct.hActive")?.getAttribute("data-value") || "";
			const hClothesTypeDetail = event.target.getAttribute("data-value");
			
			updateItem(hGender, hClothesType, hClothesTypeDetail);
        }
    });

    // 모달 외부 클릭 시 닫기
    window.addEventListener("click", function (event) {
        if (event.target === modal) {
            closeCategoryModal();
        }
    });
});

</script>
</html>
