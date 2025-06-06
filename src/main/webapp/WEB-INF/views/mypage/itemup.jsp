<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상품 등록</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<div class="body-container">
    <h1>상품 등록</h1>
    <form method="POST" enctype="multipart/form-data" onsubmit="return validateForm(event)" class="itemUp">

        <!-- 카테고리 선택 -->
        <div>
            <label for="categoryLevel1">성별 선택</label>
            <select id="categoryLevel1" onchange="updateCategoryLevel2()">
                <option value="">성별 선택</option>
                <option value="10">남성</option>
                <option value="20">여성</option>
            </select>
        </div>

        <div>
            <label for="categoryLevel2">중분류 선택</label>
            <select id="categoryLevel2" onchange="updateCategoryLevel3()">
                <option value="">중분류 선택</option>
            </select>
        </div>

        <div>
            <label for="categoryLevel3">소분류 선택</label>
            <select id="categoryLevel3">
                <option value="">소분류 선택</option>
            </select>
        </div>
		<input type="hidden" id="categoryNo" name="categoryNo">
		<input type="hidden" id="userToken" name="userToken">
        <!-- 나머지 상품 등록 필드들 -->
        <div>
            <label for="itemTitle">제목</label>
            <input type="text" id="itemTitle" name="itemTitle" required>
        </div>

        <div class="uploadImage">
            <label for="imageFile">이미지</label>
            <input type="file" id="imageFile" name="imageFiles[]" accept="image/*" required multiple>
        </div>

        <div class="startEnd">
            <label for="itemStartDate">경매 시작시간</label>
            <input type="datetime-local" id="itemStartDate" name="itemSaleStartDate" required>
            <label for="itemEndDate">경매 마감시간</label>
            <input type="datetime-local" id="itemEndDate" name="itemSaleEndDate" required>
        </div>

        <div>
            <label for="itemSaleType">판매타입</label><br>
            <label for="itemSaleType0"><input type="radio" id="itemSaleType0" name="itemSaleType" value="AUCTION" required>경매</label>
            <!-- <label for="itemSaleType1"><input type="radio" id="itemSaleType1" name="itemSaleType" value="SHOP" required>쇼핑</label> 개인은 판매타입을 쇼핑으로 설정할 수 없음 -->
            <label for="itemSaleType2"><input type="radio" id="itemSaleType2" name="itemSaleType" value="EXCHANGE" required>교환</label>
            <label for="itemSaleType3"><input type="radio" id="itemSaleType3" name="itemSaleType" value="DONATION" required>기부</label>
        </div>

        <label for="description" class="uploadContent">내용</label>
        <textarea id="description" name="itemContent" rows="4" cols="50" required></textarea><br>

        <label for="itemQuality">상품품질</label>
        <div class="uploadCate">
            <label for="itemQuality0"><input type="radio" id="itemQuality0" name="itemQuality" value="HIGH" required>상</label>
            <label for="itemQuality1"><input type="radio" id="itemQuality1" name="itemQuality" value="MEDIUM" required>중</label>
            <label for="itemQuality2"><input type="radio" id="itemQuality2" name="itemQuality" value="LOW" required>하</label>
        </div>

        <div>
            <label for="itemQuantity">상품수량</label>
            <input type="number" id="itemQuantity" name="itemQuantity" required="required">
        </div>

        <div>
            <label for="itemPrice">가격</label>
            <input type="number" id="itemPrice" name="itemPrice" required>
        </div>
        
        <input type="text" id="address" placeholder="주소" disabled>
    	<input type="button" onclick="searchAddress()" value="주소 검색">
    	<br>
   		<input type="text" id="detail_address" placeholder="상세주소">
    	<input type="hidden" id="position" name="position">
    	<br>
    	<div id="map" style="width:700px;height:400px;margin-top:10px;display:none"></div>

        <input type="submit" value="등록" class="uploadBtn">
    </form>

    <a href="/mypage" class="toMypage">마이페이지로 돌아가기</a>
</div>
<jsp:include page="../footer.jsp"/>
</body>
<script>
	window.addEventListener("DOMContentLoaded", function() {
	    const userTokenInput = document.getElementById("userToken");
	    const auth = localStorage.getItem("Token");
	
	    if (userTokenInput && auth) {
	    	userTokenInput.value = auth;
	    }
	});
</script>
<script>
	// 페이지 로드 시 오늘 날짜를 yyyy-MM-dd 형식으로 설정
	window.onload = function() {
	  var today = new Date();
	  var yyyy = today.getFullYear();
	  var mm = (today.getMonth() + 1).toString().padStart(2, '0');  // 월은 0부터 시작하므로 +1
	  var dd = today.getDate().toString().padStart(2, '0');  // 일(day)을 2자리로 만들기 위해 padding
	
	  var todayFormatted = yyyy + '-' + mm + '-' + dd;
	
	  // itemStart와 itemEnd의 min 속성에 오늘 날짜를 설정
	  document.getElementById('itemStartDate').setAttribute('min', todayFormatted + "T00:00");
	  document.getElementById('itemEndDate').setAttribute('min', todayFormatted + "T00:00");
	}
	
	// 경매 시작시간이 변경되면 경매 마감시간의 최소값을 설정
	function updateEndTime() {
	  var itemStartDate = document.getElementById('itemStartDate').value;
	  var itemEndDate = document.getElementById('itemEndDate').value;
	
	  // 경매 시작시간이 설정되면 마감시간의 최소값을 경매 시작시간으로 설정
	  document.getElementById('itemEndDate').setAttribute('min', itemStartDate);
	
	  // 경매 마감시간이 경매 시작시간보다 앞설 경우 알림창 띄우기
	  if (itemEndDate) {
	    var startTime = new Date(itemStartDate);
	    var endTime = new Date(itemEndDate);
	
	    if (endTime < startTime) {
	      alert("경매 마감시간은 경매 시작시간보다 이전일 수 없습니다. 마감시간을 다시 설정해주세요.");
	      // 경매 마감시간을 경매 시작시간으로 강제 설정
	      document.getElementById('itemEndDate').value = itemStartDate;
	    }
	  }
	}
	
	// itemStart와 itemEnd의 시간값이 변경될 때마다 updateEndTime 함수 호출
	document.getElementById('itemStartDate').addEventListener('input', updateEndTime);
	document.getElementById('itemEndDate').addEventListener('input', updateEndTime);
	
	function validateForm(event) {
	  var itemStartDate = document.getElementById('itemStartDate').value;
	  var itemEndDate = document.getElementById('itemEndDate').value;
	
	  // 경매 마감시간이 경매 시작시간보다 앞설 경우
	  if (new Date(itemEndDate) < new Date(itemStartDate)) {
	    alert("경매 마감시간은 경매 시작시간보다 이전일 수 없습니다. 마감시간을 다시 설정해주세요.");
	    event.preventDefault();
	    return false;
	  }
	
	  return true;
	}
	
</script>
<script>
     // 카테고리 데이터
     const categories = {
         "10": { // 남성
             "01": { name: "아우터", subcategories: {
                 "00": "전체", "01": "블루종", "02": "레더 재킷", "03": "카디건", "04": "트러커 재킷", "05": "블레이저",
                 "06": "스타디움", "07": "나일론/아노락 재킷", "08": "트레이닝", "09": "사파리/헌팅 재킷",
                 "10": "베스트/패딩베스트", "11": "숏패딩/숏헤비", "12": "퍼/무스탕/플리스", "13": "롱패딩/롱헤비",
                 "14": "숏코트", "15": "롱코트", "16": "트렌치코트", "17": "기타"
             }},
             "02": { name: "상의", subcategories: {
                 "00": "전체", "01": "맨투맨", "02": "후드", "03": "셔츠/블라우스", "04": "긴소매 티셔츠",
                 "05": "반소매 티셔츠", "06": "카라 티셔츠", "07": "니트", "08": "민소매 티셔츠", "09": "기타"
             }},
             "03": { name: "바지", subcategories: {
                 "00": "전체", "01": "데님", "02": "트레이닝", "03": "코튼", "04": "슬랙스", "05": "숏",
                 "06": "레깅스", "07": "점프슈트", "08": "기타"
             }},
             "06": { name: "가방", subcategories: {
                 "00": "전체", "01": "메신저백", "02": "크로스백", "03": "숄더백", "04": "백팩", "05": "토트백",
                 "06": "에코백", "07": "보스턴백", "08": "파우치", "09": "캐리어", "10": "지갑", "11": "클러치백",
                 "12": "기타"
             }},
             "07": { name: "패션소품", subcategories: {
                 "00": "전체", "01": "모자", "02": "머플러", "03": "주얼리", "04": "양말", "05": "아이웨어",
                 "06": "시계", "07": "벨트", "08": "기타"
             }},
             "08": { name: "신발", subcategories: {
                 "00": "전체", "01": "스니커즈", "02": "패딩/퍼 신발", "03": "부츠/워커", "04": "구두",
                 "05": "샌들", "06": "슬리퍼", "07": "스포츠화", "08": "기타"
             }}
         },
         "20": { // 여성
             "01": { name: "아우터", subcategories: {
                 "00": "전체", "01": "블루종", "02": "레더 재킷", "03": "카디건", "04": "트러커 재킷",
                 "05": "블레이저", "06": "스타디움", "07": "나일론/아노락 재킷", "08": "트레이닝",
                 "09": "사파리/헌팅 재킷", "10": "베스트/패딩베스트", "11": "숏패딩/숏헤비", "12": "퍼/무스탕/플리스",
                 "13": "롱패딩/롱헤비", "14": "숏코트", "15": "롱코트", "16": "트렌치코트", "17": "기타"
             }},
             "02": { name: "상의", subcategories: {
                 "00": "전체", "01": "맨투맨", "02": "후드", "03": "셔츠/블라우스", "04": "긴소매 티셔츠",
                 "05": "반소매 티셔츠", "06": "카라 티셔츠", "07": "니트", "08": "민소매 티셔츠", "09": "기타"
             }},
             "03": { name: "바지", subcategories: {
                 "00": "전체", "01": "데님", "02": "트레이닝", "03": "코튼", "04": "슬랙스", "05": "숏",
                 "06": "레깅스", "07": "점프슈트", "08": "기타"
             }},
             "04": { name: "원피스", subcategories: {
                 "00": "전체", "01": "미니", "02": "미디", "03": "맥시", "04": "기타"
             }},
             "05": { name: "스커트", subcategories: {
                 "00": "전체", "01": "미니", "02": "미디", "03": "맥시", "04": "기타"
             }},
             "06": { name: "가방", subcategories: {
                 "00": "전체", "01": "메신저백", "02": "크로스백", "03": "숄더백", "04": "백팩", "05": "토트백",
                 "06": "에코백", "07": "보스턴백", "08": "파우치", "09": "캐리어", "10": "지갑", "11": "클러치백",
                 "12": "기타"
             }},
             "07": { name: "패션소품", subcategories: {
                 "00": "전체", "01": "모자", "02": "머플러", "03": "주얼리", "04": "양말", "05": "아이웨어",
                 "06": "시계", "07": "벨트", "08": "기타"
             }},
             "08": { name: "신발", subcategories: {
                 "00": "전체", "01": "스니커즈", "02": "패딩/퍼 신발", "03": "부츠/워커", "04": "구두",
                 "05": "샌들", "06": "슬리퍼", "07": "스포츠화", "08": "기타"
             }}
         }
     };

     // 중분류 업데이트 함수
     function updateCategoryLevel2() {
         const level1 = document.getElementById("categoryLevel1").value;
         const level2 = document.getElementById("categoryLevel2");
         const level3 = document.getElementById("categoryLevel3");

         // 중분류, 소분류 초기화
         level2.innerHTML = "<option value=''>중분류 선택</option>";
         level3.innerHTML = "<option value=''>소분류 선택</option>"; // 소분류 초기화

         if (level1 === "") return; // 대분류가 선택되지 않았으면 중단

         const subcategories = categories[level1];

         // 중분류 이름 추가
         for (let key in subcategories) {
             let option = document.createElement("option");
             option.value = key;
             option.textContent = subcategories[key].name; // 중분류 이름 표시
             level2.appendChild(option);
         }
     }

     // 소분류 업데이트 함수
     function updateCategoryLevel3() {
         const level1 = document.getElementById("categoryLevel1").value;
         const level2 = document.getElementById("categoryLevel2").value;
         const level3 = document.getElementById("categoryLevel3");

         level3.innerHTML = "<option value=''>소분류 선택</option>"; // 소분류 초기화

         if (level2 === "") return; // 중분류가 선택되지 않았으면 중단

         const subcategories = categories[level1][level2].subcategories;

         // 소분류 추가
         for (let key in subcategories) {
             let option = document.createElement("option");
             option.value = key;
             option.textContent = subcategories[key]; // 소분류 이름 표시
             level3.appendChild(option);
         }
    }
	      
	function validateForm(event) {
	    // 예: categoryLevel3 → categoryNo
	    const level3 = document.getElementById("categoryLevel3").value;
	    const level2 = document.getElementById("categoryLevel2").value;
	    const level1 = document.getElementById("categoryLevel1").value;
	
	    if (!level1 || !level2 || !level3) {
	        alert("카테고리를 모두 선택해주세요.");
	        event.preventDefault();
	        return false;
	    }
	
	    // 개인경매 + 대분류 + 중분류 + 소분류
	    let categoryNo = "10" + level1 + level2 + level3;
	    
	    // hidden input에 categoryNo 값 설정
	    document.getElementById("categoryNo").value = categoryNo;
	
	    return true; // 유효성 검사를 통과하면 submit 진행
	}
</script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=4e78a0563c35e7666f6bede4cee92957&libraries=services"></script>
<script>
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new daum.maps.LatLng(37.537187, 127.005476), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

    // 지도를 미리 생성
    var map = new daum.maps.Map(mapContainer, mapOption);
    // 주소-좌표 변환 객체를 생성
    var geocoder = new daum.maps.services.Geocoder();
    // 마커를 미리 생성
    var marker = new daum.maps.Marker({
        position: new daum.maps.LatLng(37.537187, 127.005476),
        map: map
    });

    function searchAddress() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address; // 최종 주소 변수

                document.getElementById("address").value = addr;
                geocoder.addressSearch(addr, function(results, status) {
                    // 정상적으로 검색이 완료됐으면
                    if (status === daum.maps.services.Status.OK) {
                        var result = results[0]; // 첫번째 결과의 값을 활용
                        var coords = new daum.maps.LatLng(result.y, result.x);
                        mapContainer.style.display = "block";
                        map.relayout();
                        map.setCenter(coords); // 지도 중심 변경
                        marker.setPosition(coords); // 마커를 결과값으로 받은 위치로 이동
                        
                        document.getElementById("position").value = result.y + " " + result.x;
                    }
                });
            }
        }).open();
    }
</script>
</html>
