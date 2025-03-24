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
	    <img src="http://192.168.0.41:8080/image/${item_no}" alt="상품 이미지">
	    <h2>상품명</h2>
	    <p>가격: ₩xx,xxx</p>
	</div>
	<div class="review-form">
	    <label>별점:</label>
	    <div class="star-rating">
	        <input type="radio" id="star5" name="rating" value="5"><label for="star5">⭐</label>
	        <input type="radio" id="star4" name="rating" value="4"><label for="star4">⭐</label>
	        <input type="radio" id="star3" name="rating" value="3"><label for="star3">⭐</label>
	        <input type="radio" id="star2" name="rating" value="2"><label for="star2">⭐</label>
	        <input type="radio" id="star1" name="rating" value="1"><label for="star1">⭐</label>
	    </div>
	
	    <textarea id="reviewText" placeholder="리뷰를 입력하세요"></textarea>
	    <button onclick="submitReview()">리뷰 작성</button>
	</div>
</div>
</body>
</html>