<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="isLoggedIn" value="${not empty user}" />
<c:set var="userNo" value="${user != null ? user.userNo : -1}" />
<%@ include file="../header.jsp"%>
<!DOCTYPE html>
<html>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<head>
<meta charset="UTF-8">
<title>${item.itemTitle}</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<body>
	<div class="item-detail-container">
		<div class="item-header">
			<div class="item-title-section">
				<h1 class="item-name">${item.itemTitle}</h1>
				<button id="favor-btn" data-item-no="${item.itemNo}">☆</button>
			</div>
			<div class="item-action-buttons">
				<button id="edit-btn" class="action-btn" hidden="hidden">수정</button>
				<button id="delete-btn" class="action-btn" hidden="hidden">삭제</button>
				<button id="chatList" class="qna-btn">문의하기</button>
			</div>
		</div>

		<div class="item-content">
			<div class="item-gallery">
				<c:if test="${item.imageCount > 0}">
					<img id="main-image" class="gallery-main-image"
						src="/image/${item.itemNo}/0" alt="메인 이미지">
					<div class="gallery-thumbnails">
						<c:forEach begin="0" end="${item.imageCount-1}" var="index">
							<img src="/image/${item.itemNo}/${index}" alt="섬네일 ${index+1}"
								class="gallery-thumbnail" onclick="changeMainImage(this.src)">
						</c:forEach>
					</div>
				</c:if>
				<c:if test="${item.imageCount == 0}">
					<div class="gallery-main-image"
						style="display: flex; align-items: center; justify-content: center; color: #999">
						이미지가 없습니다</div>
				</c:if>
			</div>

			<div class="item-info-section">
				<div class="time-section">
					<span id=remain-time-heading>경매 종료까지 남은 시간</span>
					<span id="remain-time">계산 중</span>
				</div>

				<div class="bid-section">
					<h2 id="price-heading">현재가</h2>
					<span id="price">${item.getFormattedPrice(item.itemPrice)} 원</span>

					<div>
						<label for="price-heading">희망 입찰가</label>
						<div class="bid-input-group">
							<input type="number" id="price-text" min="0" step="100"
								placeholder="입찰 금액을 입력하세요"> <input type="submit"
								id="bid-btn" value="입찰">
						</div>
					</div>

					<button onclick="openWindow('BidlistWindow', '/')" id="bid-list">입찰 기록 보기</button>
				</div>
			</div>
		</div>

		<div class="details-section">
			<h3>상품 상세 정보</h3>
			<div class="details-grid">
				<div class="detail-item">
					<span class="detail-label">판매자</span>
					<span class="detail-value">
						<a href="${pageContext.request.contextPath}/user/profile/${item.sellerNo}">${item.sellerNick}</a>
					</span>
				</div>

				<div class="detail-item">
					<span class="detail-label">카테고리</span> <span class="detail-value">${item.categoryName}</span>
				</div>

				<div class="detail-item">
					<span class="detail-label">경매 기간</span>
					<span class="detail-value">
						${(item.itemSaleStartDate != null) ? item.getFormattedDate(item.itemSaleStartDate).concat(" ~ ") : ""}
						${(item.itemSaleEndDate != null) ? item.getFormattedDate(item.itemSaleEndDate) : "무기한"}
						<span id="status">${item.itemStatus}</span>
					</span>
				</div>

				<c:if test="${item.itemQuality != null}">
					<div class="detail-item">
						<span class="detail-label">품질</span>
						<span class="detail-value">${item.itemQuality}</span>
					</div>
				</c:if>

				<div class="detail-content">${item.itemContent}</div>
			</div>
		</div>

		<button onclick="window.history.back()" class="back-button">뒤로가기</button>
	</div>


	<jsp:include page="../sidebar.jsp" />
	<jsp:include page="../footer.jsp" />
</body>
<script src="/js/recent/config.js"></script>
<script src="/js/recent/addRecent.js"></script>
<script src="/js/recent/getRecent_sidebar.js"></script>
<script src="/js/favor/favorItem.js"></script>
<script src="/js/chat/chatRoomList.js"></script>
<script>
	function addCommas(num) {
	    if (isNaN(num)) {
	        return num;
	    }
	
	    num = Number(num).toString();
	    return num.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
</script>
<script>
	const isLoggedIn = ${isLoggedIn};
	const userNo = ${userNo};
	const itemNo = ${item.itemNo};
	const sellerNo = ${item.sellerNo};
	const itemStart = ${item.itemSaleStartDate};
	const editBtn = document.getElementById("edit-btn");
	const deleteBtn = document.getElementById("delete-btn");
	const bidBtn = document.getElementById("bid-btn");
	const priceText = document.getElementById("price-text");
	let intervals = [];
	let status = "";
	let remainTime = 0;

	document.addEventListener("DOMContentLoaded", async function() {
		console.log(itemStart);
		console.log();
		if(isLoggedIn) {
			initIfOwner(userNo, sellerNo);
		}
		updateStatus(itemNo);
		//itemDetail 페이지 타입별 분리시, itemSaleType 검사하는 if 조건 제거
		if(`${item.itemSaleType}` === "경매" && `${item.itemSaleEndDate}` != null) {
			await updateStatus(itemNo);
			let inner = await updateRemainTime(itemNo);
			await inner();
			
			startInterval(() => updatePrice(itemNo), 1000);
			startInterval(inner, 1000);
		}
	});
	
	function initIfOwner(userNo, sellerNo) {
		if (userNo === sellerNo) {
            priceText.disabled = true;
            bidBtn.disabled = true;
            editBtn.hidden = false;
            deleteBtn.hidden = false;
		}
	}
	
	function startInterval(f, s) {
	    let interval = setInterval(f, s);
	    intervals.push(interval);
	}
    
    function stopAllIntervals() {
        intervals.forEach(id => clearInterval(id));
        intervals = [];
    }
    
    function changeMainImage(src) {
        document.getElementById('main-image').src = src;
    }
    
    priceText.addEventListener("keypress", function(e) {
        if (!/^\d$/.test(e.key) && e.key !== "Backspace" && e.key !== "Tab") {
            e.preventDefault();
        }
    });

    priceText.addEventListener("input", function() {
        this.value = this.value.replace(/[^\d]/g, "");
    });

    bidBtn.addEventListener("click", function() {
        const price = priceText.value;
        
        if (!price.trim() || isNaN(Number(price))) {
            return;
        }
        if (${empty item.pointBalance}) {
        	Swal.fire({
      			title: "포인트 계정 검색 오류",
      			html: `<p>포인트 계정이 없습니다.</p>
      				   <p>포인트 페이지로 이동하시겠습니까?</p>`,
				icon: "error",
				showCancelButton: true,
				confirmButtonText: "이동",
				cancelButtonText: "취소"
      		});
        	//point_account url
        	return;
        }
        
        const pointBalance = parseFloat("${item.pointBalance}");
        if (pointBalance < price) {
        	Swal.fire({
        		title: "포인트 잔액 부족",
        		html: "현재 포인트 잔액은 <b>" + addCommas(pointBalance) + "원</b>입니다.",
        		icon: "error",
        		confirmButtonText: "확인"
        	});
            return;
        }
        
        Swal.fire({
        	title: "입찰 확인",
        	html: "<p>현재 포인트 잔액은 <b>" + addCommas(pointBalance) + "원</b>입니다.</p>"
        		+ "<p><b>" + addCommas(price) + "원</b>으로 입찰하시겠습니까?</p>",
        	icon: "question",
        	showCancelButton: true,
        	confirmButtonText: "입찰",
        	cancelButtonText: "취소"
        }).then((result) => {
       		if (result.isConfirmed) {
       			fetch(`/item/detail/${itemNo}/bid`, {
                     method: "PATCH",
                     headers: {
             	     	 "Authorization": `Bearer ${localStorage.getItem("Token")}`,
                         "Content-Type": "application/json"
                     },
                     body: JSON.stringify({ newPrice: price })
				})
				.then(response => {
					if (!response.ok) {
						return response.text()
						.then(message => { throw new Error(message); });
					}
					return response.text();
                 })
                 .then(data => {
					Swal.fire("입찰 완료", data, "success");
					})
					.catch(error => {
					Swal.fire("오류", error.message, "error");
				});
			}
		});
    });
	
    function openWindow(name, url) {
        window.open(
            url, name, "width=650,height=800,scrollbars=yes"
        );
    }

    function updatePrice(itemNo) {
        fetch(`/item/detail/${itemNo}/price`)
            .then(response => response.text())
            .then(data => {
                document.getElementById("price").innerText = addCommas(data) + " 원";
            })
    }

    async function updateStatus(itemNo) {
    	try {
	        let response = await fetch(`/item/detail/${itemNo}/status`);
	        let data = await response.text();
	        status = data;
	
	        if (data === "판매완료" || data === "종료") {
	            stopAllIntervals();

				document.getElementById("remain-time-heading").innerText = "";
	            const priceHeading = document.getElementById("price-heading");
	            const heading = (data === "예약중" || data === "판매완료") ? "낙찰가" : "종료가";
	            priceHeading.childNodes[0].textContent = heading;
	        }
	    	
	        if (data != "판매중") {
	            priceText.disabled = true;
	            bidBtn.disabled = true;
	        }

	        document.getElementById("status").innerText = data;
    	} catch (error) {
            console.error("updateStatus(itemNo): ", error);
        }
    }
	
	async function initRemainTime(itemNo, type) {
		const response = await fetch(`/item/detail/${itemNo}/remainTime/` + type);
        const data = await response.json();
        const typeText = (type === "start") ? "시작" : "종료";
        remainTime = data;
        document.getElementById("remain-time-heading").innerText = "경매 " + typeText + "까지 남은 시간";
	}

	async function updateRemainTime(itemNo) {
    	let type = (status === "대기중") ? "start" : "end";
    	await initRemainTime(itemNo, type);
    	
    	let inner = async function() {
    		if (remainTime <= 0) {
    			await updateStatus(itemNo);
    			
    			if (status !== "판매중") {
    				return;
    			} else {
    				await initRemainTime(itemNo, "end");    				
    			}
    		}

    		let days = Math.floor(remainTime / 86400);
            let hours = Math.floor((remainTime % 86400) / 3600);
            let minutes = Math.floor((remainTime % 3600) / 60);
            let seconds = remainTime % 60;

            let timeText = "";
            if (days > 0) {
            	timeText += days + "일 ";
            }
            if (hours > 0) {
            	timeText += hours + "시간 ";
            }
            if (minutes > 0) {
            	timeText += minutes + "분 ";
            }
            timeText += seconds + "초";

            document.getElementById("remain-time").innerText = timeText;
            remainTime--;
        };
        
        return inner;
    }
</script>
</html>