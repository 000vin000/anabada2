<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<body>
	<div id="categorySearchOption">
		<%-- 카테고리 선택 --%>
		<select id="gender" name="gender">
			<option value="" selected>전체</option>
			<option value="m">남성</option>
			<option value="w">여성</option>
		</select>
		<select id="clothesType">
			<option value="" selected>전체</option>
			<option value="top">상의</option>
			<option value="bottom">하의</option>
			<option value="outer">아우터</option>
			<option value="dress">원피스</option>
			<option value="etc">기타</option>
			<option value="set">세트상품</option>
		</select>
		<input type="button" id="cate" value="검색" onclick="updateItem()">
	</div>
	<div id="sortOption">
		<select id="sortOrder" name="sortOrder" onchange="sortItems()">
		<option value="" selected>기본순</option>
			<option value="new">신규경매순</option>    
			<option value="deadline">마감임박순</option>
			<option value="popular">인기순</option>
			<option value="asc">낮은가격순</option>
			<option value="desc">높은가격순</option>
		</select>
	</div>
	<div id="itemOption">
		<ul>
			<c:if test="${not empty itemList}">
				<c:forEach var="item" items="${itemList}">
					<li>
						<a href="/item/detail/${item.itemNo}" id="card">
							<img src="data:image/png;base64,${item.base64Image}" alt="${item.itemName} 이미지"/>
							<hr id="line">
							<p id="itemName">${item.itemName}</p> 
							<hr id="line">
							<p id="itemPrice">${item.addCommas(item.itemPrice)}원 </p>
							<p id="itemUserNick">${item.userNick} | 입찰 ${item.bidCount}회</p>
							<p id="itemAuction">${item.getItemAuctionStr(item.itemAuction)}</p>
						</a>
					</li>
				</c:forEach>
			</c:if>
			<c:if test="${empty itemList }">
				<div>${error}</div>
			</c:if>
		</ul>
	</div>
</body>