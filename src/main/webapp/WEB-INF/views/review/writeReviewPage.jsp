<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ANABADA</title>
</head>
<body>
<div class="body-container">
	<div class="product-info">
	    <img src="http://192.168.0.41:8080/image/${item_no}" alt="${item_title}">
	    <h2>${item_title}</h2>
	</div>
	<form id="reviewForm">
	  <div>
	    <label>별점:</label>
	    <div id="reviewRating">
	      <!-- 별점: 0.5 ~ 5.0까지 0.5 단위 -->
	      <span>
	        <input type="radio" id="star5" name="reviewRating" value="5.0"><label for="star5">★</label>
	        <input type="radio" id="star4_5" name="reviewRating" value="4.5"><label for="star4_5">☆</label>
	        <input type="radio" id="star4" name="reviewRating" value="4.0"><label for="star4">★</label>
	        <input type="radio" id="star3_5" name="reviewRating" value="3.5"><label for="star3_5">☆</label>
	        <input type="radio" id="star3" name="reviewRating" value="3.0"><label for="star3">★</label>
	        <input type="radio" id="star2_5" name="reviewRating" value="2.5"><label for="star2_5">☆</label>
	        <input type="radio" id="star2" name="reviewRating" value="2.0"><label for="star2">★</label>
	        <input type="radio" id="star1_5" name="reviewRating" value="1.5"><label for="star1_5">☆</label>
	        <input type="radio" id="star1" name="reviewRating" value="1.0"><label for="star1">★</label>
	        <input type="radio" id="star0_5" name="reviewRating" value="0.5"><label for="star0_5">☆</label>
	      </span>
	    </div>
	  </div>
	
	  <div>
	    <label for="reviewContent">리뷰 내용:</label><br>
	    <textarea id="reviewContent" name="reviewContent" rows="4" cols="50"></textarea>
	  </div>
	
	  <button type="submit">리뷰 등록</button>
	</form>
</div>
</body>
<script src="/js/review/writeReview.js"></script>
</html>