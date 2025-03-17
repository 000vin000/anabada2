<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>상품등록</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
<script>
  // 페이지 로드 시 오늘 날짜를 yyyy-MM-dd 형식으로 설정
  window.onload = function() {
    var today = new Date();
    var yyyy = today.getFullYear();
    var mm = (today.getMonth() + 1).toString().padStart(2, '0');  // 월은 0부터 시작하므로 +1
    var dd = today.getDate().toString().padStart(2, '0');  // 일(day)을 2자리로 만들기 위해 padding

    var todayFormatted = yyyy + '-' + mm + '-' + dd;

    // itemStart와 itemEnd의 min 속성에 오늘 날짜를 설정
    document.getElementById('itemStart').setAttribute('min', todayFormatted + "T00:00");
    document.getElementById('itemEnd').setAttribute('min', todayFormatted + "T00:00");
  }

  // 경매 시작시간이 변경되면 경매 마감시간의 최소값을 설정
  function updateEndTime() {
    var itemStart = document.getElementById('itemStart').value;
    var itemEnd = document.getElementById('itemEnd').value;

    // 경매 시작시간이 설정되면 마감시간의 최소값을 경매 시작시간으로 설정
    document.getElementById('itemEnd').setAttribute('min', itemStart);

    // 경매 마감시간이 경매 시작시간보다 앞설 경우 알림창 띄우기
    if (itemEnd) {
      var startTime = new Date(itemStart);
      var endTime = new Date(itemEnd);

      if (endTime < startTime) {
        alert("경매 마감시간은 경매 시작시간보다 이전일 수 없습니다. 마감시간을 다시 설정해주세요.");
        // 경매 마감시간을 경매 시작시간으로 강제 설정
        document.getElementById('itemEnd').value = itemStart;
      }
    }
  }

  // itemStart와 itemEnd의 시간값이 변경될 때마다 updateEndTime 함수 호출
  document.getElementById('itemStart').addEventListener('input', updateEndTime);
  document.getElementById('itemEnd').addEventListener('input', updateEndTime);

  function validateForm(event) {
    var itemStart = document.getElementById('itemStart').value;
    var itemEnd = document.getElementById('itemEnd').value;

    // 경매 마감시간이 경매 시작시간보다 앞설 경우
    if (new Date(itemEnd) < new Date(itemStart)) {
      alert("경매 마감시간은 경매 시작시간보다 이전일 수 없습니다. 마감시간을 다시 설정해주세요.");
      event.preventDefault();
      return false;
    }

    return true;
  }
</script>
</head>
<body>
<div class="body-container">
<h1>상품 등록</h1>
<form method="POST" enctype="multipart/form-data" onsubmit="return validateForm(event)" class="itemUp">
  <div>
  <label for="itemName">제목</label>
  <input type="text" id="itemName" name="itemName" required>
  </div>

  <div class="uploadImage">
  <label for="imageFile">이미지</label>
  <input type="file" id="imageFile" name="imageFiles[]" accept="image/*" required multiple>
  </div>
  
  <div class="startEnd">
  <label for="itemStart">경매 시작시간</label>
  <input type="datetime-local" id="itemStart" name="itemStart" required>
  <label for="itemEnd" id="tabEnd">경매 마감시간</label>
  <input type="datetime-local" id="itemEnd" name="itemEnd" required>
  </div>
	
  <label>카테고리</label><br>
  <label for="itemGender">성별</label>
  <div class="uploadCate">
  <label for="itemGender0"><input type="radio" id="itemGender0" name="itemGender" value="m" required>남성</label>
  <label for="itemGender1"><input type="radio" id="itemGender1" name="itemGender" value="w" required>여성</label>
  </div>
  
  <label for="itemCate">의류 종류</label>
  <div class="uploadCate">
  <label for="itemCate0"><input type="radio" id="itemCate0" name="itemCate" value="top" required>상의</label>
  <label for="itemCate1"><input type="radio" id="itemCate1" name="itemCate" value="bottom" required>하의</label>
  <label for="itemCate2"><input type="radio" id="itemCate2" name="itemCate" value="outer" required>아우터</label>
  <label for="itemCate3"><input type="radio" id="itemCate3" name="itemCate" value="dress" required>원피스</label>
  <label for="itemCate4"><input type="radio" id="itemCate4" name="itemCate" value="etc" required>기타</label>
  <label for="itemCate5"><input type="radio" id="itemCate5" name="itemCate" value="set" required>셋업</label>
  </div>
  
  <div class="uploadPrice">
  <label for="itemPrice" class="uploadContent">가격</label>
  <input type="number" id="itemPrice" name="itemPrice" required>
  </div>

  <label for="itemContent" class="uploadContent">내용</label>
  <textarea id="description" name="itemContent" rows="4" cols="50" required></textarea><br>

  <label for="itemStatus">상품상태</label>
  <div class="uploadCate">
  <label for="itemStatus0"><input type="radio" id="itemStatus0" name="itemStatus" value="high" required>상</label>
  <label for="itemStatus1"><input type="radio" id="itemStatus1" name="itemStatus" value="mid" required>중</label>
  <label for="itemStatus2"><input type="radio" id="itemStatus2" name="itemStatus" value="low" required>하</label>
  </div>
  
  <input type="submit" value="등록" class="uploadBtn">
</form>
<a href="/mypage" class="toMypage">마이페이지로 돌아가기</a>
</div>
<jsp:include page="../footer.jsp"/>
</body>
</html>