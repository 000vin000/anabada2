<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>상품 수정</title>
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

    // itemStart의 시간값이 변경될 때마다 updateEndTime 함수 호출
    document.getElementById('itemStart').addEventListener('input', updateEndTime);

    // itemEnd의 시간값이 변경될 때에도 updateEndTime 함수 호출
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
<h1>상품 수정</h1>
  <!-- 기존 이미지들 및 삭제 폼 -->
  <label style="font-weight: bold; font-size: 18px; color: #21afbf; margin-right: 5px;">기존 이미지</label>
  <ul>
    <c:forEach var="image" items="${images}" varStatus="status">
        <img src="data:image/png;base64,${base64Images[status.index]}" alt="Image" style="max-width: 100px; height: auto;">
        <!-- 이미지 삭제 버튼 -->
        <form action="/mypage/itemupdate/deleteImage/${image.imageNo}/${item.itemNo}" method="post" style="display:inline;" onsubmit="return confirmDelete();">
          <button type="submit" style="background-color: #ccc; border: none; color: white; cursor: pointer;">X</button>
        </form>
    </c:forEach>
  </ul>
  
  <script>
  function confirmDelete() {
    return confirm("정말 이 이미지를 삭제하시겠습니까?");
  }
</script>

<!-- 새 이미지 업로드 -->
<form action="/mypage/itemupdate/${item.itemNo}" method="post" enctype="multipart/form-data" onsubmit="return validateForm(event);" class="itemUp">
  <div class="uploadImage">
  <input type="hidden" name="itemNo" value="${item.itemNo}">
  <label for="imageFile">이미지</label>
  <input type="file" id="imageFile" name="imageFiles[]" accept="image/*" multiple>
  </div>
  
  <div>
  <label for="itemName">제목</label>
  <input type="text" id="itemName" name="itemName" value="${item.itemName}" required>
  </div>
  
  <div class="startEnd">
  <label for="itemStart">경매 시작시간</label>
  <input type="datetime-local" id="itemStart" name="itemStart" value="${item.itemStart}" required>
  <label for="itemEnd" id="tabEnd">경매 마감시간</label>
  <input type="datetime-local" id="itemEnd" name="itemEnd" value="${item.itemEnd}" required>
  </div>

  <label>카테고리</label><br>
  <label for="itemGender">성별</label>
  <div class="uploadCate">
  <label for="itemGender0"><input type="radio" id="itemGender0" name="itemGender" value="m" ${item.itemGender == 'm' ? 'checked' : ''} required>남성</label>
  <label for="itemGender1"><input type="radio" id="itemGender1" name="itemGender" value="w" ${item.itemGender == 'w' ? 'checked' : ''} required>여성</label>
  </div>
  
  <label for="itemCate">의류 종류</label>
  <div class="uploadCate">
  <label for="top"><input type="radio" id="top" name="itemCate" value="top" ${item.itemCate == 'top' ? 'checked' : ''} required>상의</label>
  <label for="bottom"><input type="radio" id="bottom" name="itemCate" value="bottom" ${item.itemCate == 'bottom' ? 'checked' : ''} required>하의</label>
  <label for="outer"><input type="radio" id="outer" name="itemCate" value="outer" ${item.itemCate == 'outer' ? 'checked' : ''} required>아우터</label>
  <label for="dress"><input type="radio" id="dress" name="itemCate" value="dress" ${item.itemCate == 'dress' ? 'checked' : ''} required>원피스</label>
  <label for="etc"><input type="radio" id="etc" name="itemCate" value="etc" ${item.itemCate == 'etc' ? 'checked' : ''} required>기타</label>
  <label for="set"><input type="radio" id="set" name="itemCate" value="set" ${item.itemCate == 'set' ? 'checked' : ''} required>셋업</label>
  </div>

  <div class="uploadPrice">
  <label for="itemPrice" class="uploadContent">가격</label>
  <input type="number" id="itemPrice" name="itemPrice" value="${item.itemPrice}" required>
  </div>
  
  <label for="itemContent" class="uploadContent">내용</label>
  <textarea id="description" name="itemContent" rows="4" cols="50" required>${item.itemContent}</textarea><br>

  <label for="itemStatus">상품상태</label>
  <div class="uploadCate">
  <label for="itemStatus0"><input type="radio" id="itemStatus0" name="itemStatus" value="high" ${item.itemStatus == 'high' ? 'checked' : ''} required>상</label>
  <label for="itemStatus1"><input type="radio" id="itemStatus1" name="itemStatus" value="mid" ${item.itemStatus == 'mid' ? 'checked' : ''} required>중</label>
  <label for="itemStatus2"><input type="radio" id="itemStatus2" name="itemStatus" value="low" ${item.itemStatus == 'low' ? 'checked' : ''} required>하</label>
  </div>
  
  <input type="submit" value="수정" class="uploadBtn">
</form>
<a href="/mypage" class="toMypage">마이페이지로 돌아가기</a>
</div>
<jsp:include page="../footer.jsp"/>
</body>
</html>
