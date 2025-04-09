<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<body class="body-container">
<div id="categorySearchOption">
      <%-- 메인 카테고리 선택 --%>
		<div id="level1Selection" class="level1-container">
		    <button class="gender-option" id="gender-all" data-value="00">전체</button>
		    <button class="gender-option" id="gender-male" data-value="10">남성</button>
		    <button class="gender-option" id="gender-female" data-value="20">여성</button>
		</div>
        <div id="level2Selection" class="level2-container"></div>
        <div id="level3Selection" class="level3-container"></div>
   </div>
</body>
<script>
//Gender option 클릭 시
document.querySelectorAll(".gender-option").forEach(button => {
    button.addEventListener("click", function () {
        // 선택된 gender-value 추출
        const level1Value = this.getAttribute("data-value");
        let level2message = "";

        // level2 버튼 업데이트
        switch (level1Value) {
            case "00":
                level2message = `
                    <button class="ct" data-value="01">아우터</button>
                    <button class="ct" data-value="02">상의</button>
                    <button class="ct" data-value="03">하의</button>
                    <button class="ct" data-value="04">원피스</button>
                    <button class="ct" data-value="05">스커트</button>
                    <button class="ct" data-value="06">가방</button>
                    <button class="ct" data-value="07">패션소품</button>
                    <button class="ct" data-value="08">신발</button>
                `;
                break;
            case "10":
                level2message = `
                    <button class="ct" data-value="01">아우터</button>
                    <button class="ct" data-value="02">상의</button>
                    <button class="ct" data-value="03">하의</button>
                    <button class="ct" data-value="06">가방</button>
                    <button class="ct" data-value="07">패션소품</button>
                    <button class="ct" data-value="08">신발</button>
                `;
                break;
            case "20":
                level2message = `
                    <button class="ct" data-value="01">아우터</button>
                    <button class="ct" data-value="02">상의</button>
                    <button class="ct" data-value="03">하의</button>
                    <button class="ct" data-value="04">원피스</button>
                    <button class="ct" data-value="05">스커트</button>
                    <button class="ct" data-value="06">가방</button>
                    <button class="ct" data-value="07">패션소품</button>
                    <button class="ct" data-value="08">신발</button>
                `;
                break;
        }
        document.getElementById("level2Selection").innerHTML = level2message;

        document.querySelectorAll(".gender-option").forEach(btn => btn.classList.remove("active"));
        this.classList.add("active");
    });
});

// level2Selection에 이벤트 위임 추가 (동적 요소 클릭 가능)
document.getElementById("level2Selection").addEventListener("click", function (event) {
    if (event.target.classList.contains("ct")) {
        const level2Value = event.target.getAttribute("data-value");
        let level3message = "";

        document.querySelectorAll("#level2Selection .ct").forEach(btn => btn.classList.remove("active"));
        event.target.classList.add("active");
        
        // level3 버튼 업데이트
        switch (level2Value) {
            case "01":
                level3message = `
                    <button class="cd" data-value="sel00">전체</button>
                    <button class="cd" data-value="sel01">블루종</button>
                    <button class="cd" data-value="sel02">레더 재킷</button>
                    <button class="cd" data-value="sel03">카디건</button>
                    <button class="cd" data-value="sel04">트러커 재킷</button>
                    <button class="cd" data-value="sel05">블레이저</button>
                    <button class="cd" data-value="sel06">스타디움</button>
                    <button class="cd" data-value="sel07">나일론/아노락 재킷</button>
                    <button class="cd" data-value="sel08">트레이닝</button>
                    <button class="cd" data-value="sel09">사파리/헌팅 재킷</button>
                    <button class="cd" data-value="sel10">베스트/패딩베스트</button>
                    <button class="cd" data-value="sel11">숏패딩/숏헤비</button>
                    <button class="cd" data-value="sel12">퍼/무스탕/플리스</button>
                    <button class="cd" data-value="sel13">롱패딩/롱헤비</button>
                    <button class="cd" data-value="sel14">숏코트</button>
                    <button class="cd" data-value="sel15">롱코트</button>
                    <button class="cd" data-value="sel16">트렌치코트</button>
                    <button class="cd" data-value="sel17">기타 아우터</button>
                `;
                break;
            case "02":
                level3message = `
                    <button class="cd" data-value="sel00">전체</button>
                    <button class="cd" data-value="sel01">맨투맨</button>
                    <button class="cd" data-value="sel02">후드</button>
                    <button class="cd" data-value="sel03">셔츠/블라우스</button>
                    <button class="cd" data-value="sel04">긴소매 티셔츠</button>
                    <button class="cd" data-value="sel05">반소매 티셔츠</button>
                    <button class="cd" data-value="sel06">카라 티셔츠</button>
                    <button class="cd" data-value="sel07">니트</button>
                    <button class="cd" data-value="sel08">민소매 티셔츠</button>
                    <button class="cd" data-value="sel09">기타 상의</button>
                `;
                break;
            case "03":
                level3message = `
                    <button class="cd" data-value="sel00">전체</button>
                    <button class="cd" data-value="sel01">데님</button>
                    <button class="cd" data-value="sel02">트레이닝</button>
                    <button class="cd" data-value="sel03">코튼</button>
                    <button class="cd" data-value="sel04">슬랙스</button>
                    <button class="cd" data-value="sel05">숏</button>
                    <button class="cd" data-value="sel06">레깅스</button>
                    <button class="cd" data-value="sel07">점프슈트</button>
                    <button class="cd" data-value="sel08">기타 하의</button>
                `;
                break;
            case "04":
                level3message = `
                    <button class="cd" data-value="sel00">전체</button>
                    <button class="cd" data-value="sel01">미니</button>
                    <button class="cd" data-value="sel02">미디</button>
                    <button class="cd" data-value="sel03">맥시</button>
                    <button class="cd" data-value="sel04">기타 원피스</button>
                `;
                break;
            case "05":
                level3message = `
                    <button class="cd" data-value="sel00">전체</button>
                    <button class="cd" data-value="sel01">미니</button>
                    <button class="cd" data-value="sel02">미디</button>
                    <button class="cd" data-value="sel03">맥시</button>
                    <button class="cd" data-value="sel04">기타 스커트</button>
                `;
                break;
            case "06":
                level3message = `
                    <button class="cd" data-value="sel00">전체</button>
                    <button class="cd" data-value="sel01">메신저백</button>
                    <button class="cd" data-value="sel02">크로스백</button>
                    <button class="cd" data-value="sel03">숄더백</button>
                    <button class="cd" data-value="sel04">백팩</button>
                    <button class="cd" data-value="sel05">토트백</button>
                    <button class="cd" data-value="sel06">에코백</button>
                    <button class="cd" data-value="sel07">보스턴백</button>
                    <button class="cd" data-value="sel08">파우치</button>
                    <button class="cd" data-value="sel09">캐리어</button>
                    <button class="cd" data-value="sel10">지갑</button>
                    <button class="cd" data-value="sel11">클러치백</button>
                    <button class="cd" data-value="sel12">기타 가방</button>
                `;
                break;
            case "07":
                level3message = `
                    <button class="cd" data-value="sel00">전체</button>
                    <button class="cd" data-value="sel01">모자</button>
                    <button class="cd" data-value="sel02">머플러</button>
                    <button class="cd" data-value="sel03">주얼리</button>
                    <button class="cd" data-value="sel04">양말</button>
                    <button class="cd" data-value="sel05">아이웨어</button>
                    <button class="cd" data-value="sel06">시계</button>
                    <button class="cd" data-value="sel07">벨트</button>
                    <button class="cd" data-value="sel08">기타 액세서리</button>
                `;
                break;
            case "08":
                level3message = `
                    <button class="cd" data-value="sel00">전체</button>
                    <button class="cd" data-value="sel01">스니커즈</button>
                    <button class="cd" data-value="sel02">슬리퍼</button>
                    <button class="cd" data-value="sel03">부츠</button>
                    <button class="cd" data-value="sel04">구두</button>
                    <button class="cd" data-value="sel05">로퍼</button>
                    <button class="cd" data-value="sel06">운동화</button>
                    <button class="cd" data-value="sel07">스포츠화</button>
                    <button class="cd" data-value="sel08">기타 신발</button>
                `;
                break;
        }
        document.getElementById("level3Selection").innerHTML = level3message;

        // 활성화된 clothesType에 active 클래스 추가
        document.querySelectorAll(".cd").forEach(btn => btn.classList.remove("active"));
        event.target.classList.add("active");
    }
});

// level3Selection에서 clothesTypeDetail 클릭 시
document.getElementById("level3Selection").addEventListener("click", function (event) {
    if (event.target.classList.contains("cd")) {
        const gender = document.querySelector(".gender-option.active")?.getAttribute("data-value") || "";
        const clothesType = document.querySelector(".ct.active")?.getAttribute("data-value") || "";
        const clothesTypeDetail = event.target.getAttribute("data-value");

        // `updateItem` 함수 호출하여 폼을 전송
        updateItem(gender, clothesType, clothesTypeDetail);
    }
});
</script>