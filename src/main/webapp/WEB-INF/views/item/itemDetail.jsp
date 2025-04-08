<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="isLoggedIn" value="${not empty user}" />
<c:set var="userNo" value="${user != null ? user.userNo : -1}" />
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${item.itemTitle}</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<link rel="stylesheet" type="text/css" href="/css/chatRoom.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>
</head>
<body>
	<div class="item-detail-container" >
		<div class="item-header">
			<div class="item-title-section">
				<h1 class="item-name">${item.itemTitle}</h1>
				<button id="favor-btn" data-item-no="${item.itemNo}">☆</button>
			</div>
			<div class="item-action-buttons">
				<button id="edit-btn" class="action-btn" hidden>수정</button>
				<button id="delete-btn" class="action-btn" hidden>삭제</button>
				<script>
			        const loggedInUserNo = "${userNo}"; =
			    </script>
				<!-- 디버깅: 로그인된 사용자 번호와 판매자 번호 출력 -->
				<!-- 로그인된 사용자 번호 출력 -->
				<p>로그인된 사용자 번호: ${userNo != null ? userNo : '로그인 필요'}</p> <!-- null 처리 추가 -->
				
				<!-- 상품 상세 정보 출력 -->
				<p>판매자 번호: ${item.sellerNo}</p>

				
				<!-- 버튼 표시 조건 -->
				<c:choose>
				    <c:when test="${loggedInUserNo != null && loggedInUserNo == item.sellerNo}">
				        <button class="viewChatRoomsBtn" data-item-no="${item.itemNo}">
				            채팅방 목록 보기
				        </button>
				    </c:when>
				    <c:otherwise>
				        <button id="inquiryBtn" class="inquiryBtn" 
				            data-room-no="${roomNo}"
				             data-user-no="${loggedInUserNo}"
				            <c:if test="${empty loggedInUserNo}">data-login-required="true"</c:if>>
				            문의하기
				        </button>
				    </c:otherwise>
				</c:choose>



				

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
				<c:if test="${item.imageCount eq 0}">
					<div class="gallery-main-image"
						style="display: flex; align-items: center; justify-content: center; color: #999">
						이미지가 없습니다
					</div>
				</c:if>
			</div>

			<div class="item-info-section">
				<div id="time-section" class="time-section"
					${(item.itemStatus ne '대기중' and item.itemStatus ne '판매중') ? 'hidden' : ''}>
					<span id=remain-time-heading>경매 종료까지 남은 시간</span>
					<span id="remain-time">계산 중</span>
				</div>

				<div class="bid-section">
					<h2 id="price-heading">${item.itemStatus}</h2>
					<span id="price">${item.getFormattedPrice(item.itemPrice)} 원</span>

					<div id="price-input-section" ${item.itemStatus ne '판매중' ? 'hidden' : ''}>
						<br>
						<h2 id="price-heading">희망 입찰가</h2>
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
						${(item.itemSaleStartDate ne null) ? item.getFormattedDate(item.itemSaleStartDate).concat(" ~ ") : ""}
						${(item.itemSaleEndDate ne null) ? item.getFormattedDate(item.itemSaleEndDate) : "무기한"}
						<span id="status">${item.itemStatus}</span>
					</span>
				</div>

				<c:if test="${not empty item.itemQuality}">
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
	
	<script src="/js/recent/config.js"></script>
	<script src="/js/recent/addRecent.js"></script>
	<script src="/js/recent/getRecent_sidebar.js"></script>
	<script src="/js/favor/favorItem.js"></script>
	<script src="/js/chat/itemDetail.js"></script>
	<script src="/js/chat/chatRoom.js"></script>
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
		
		const priceInputSection = document.getElementById("price-input-section");
		const timeSection = document.getElementById("time-section");
		const editBtn = document.getElementById("edit-btn");
		const deleteBtn = document.getElementById("delete-btn");
		const bidBtn = document.getElementById("bid-btn");
		const priceText = document.getElementById("price-text");
		
		let intervals = [];
		let status = "";
	
		document.addEventListener("DOMContentLoaded", async function() {
			await updateStatus(itemNo);
			if((status === "대기중" || status === "판매중") && `${item.itemSaleEndDate}` != null) {
				await startInterval(() => updateStatus(itemNo));
				let inner = await updateRemainTime(itemNo);
				await inner();
				
				startInterval(() => updatePrice(itemNo), 1000);
				startInterval(inner, 1000);
			}
		});
		
		function startInterval(f, s) {
		    let interval = setInterval(f, s);
		    intervals.push(interval);
		}
	    
	    function stopAllIntervals() {
	        intervals.forEach(id => {
	        	clearInterval(id);
	        	console.log("stopped interval id: " + id)
	        	});
	        intervals = [];
	    }
	    
	    function changeMainImage(src) {
	        document.getElementById("main-image").src = src;
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
	<%--
	    async function updateStatus(itemNo) {
	    	try {
		        let response = await fetch(`/item/detail/${itemNo}/status`);
		        let data = await response.text();
		        status = data;
		
		        if (data !== "대기중" && data !== "판매중") {
		            stopAllIntervals();
		            priceInputSection.hidden = true;
		            timeSection.hidden = true;
		        }
	
		        document.getElementById("status").innerText = data;
		        document.getElementById("price-heading").innerText = data;
	    	} catch (error) {
	            console.error("updateStatus(itemNo): ", error);
	        }
	    }
	    --%>
	    
	    async function updateRemainTime(itemNo) {
	        response = await getRemainTime(itemNo);
	        let { remainTime } = response;
	        const { type } = response;
	        
	        let inner = async function() {
	            if (remainTime <= 0) {
	                if (type === "종료") {
	                    stopAllIntervals(); // 경매 종료 시 타이머 멈추기
	                    return;
	                } else if (await waitForStatus(itemNo, "판매중")) {
	                    remainTime = (await getRemainTime(itemNo)).remainTime;
	                    priceInputSection.hidden = false;
	                } else {
	                    stopAllIntervals(); // 실패하면 모든 Interval 정지
	                    return;
	                }
	            }

	            document.getElementById("remain-time").innerText = formatTimeText(remainTime);
	            remainTime--;
	        };
	        
	        return inner;
	    }

		
		async function getRemainTime(itemNo) {
			const response = await fetch(`/item/detail/${itemNo}/remainTime`);
	        const data = await response.json();
	        const { remainTime, type } = data;
	        document.getElementById("remain-time-heading").innerText = "경매 " + type + "까지 남은 시간";
	        return { remainTime, type };
		}
	
		async function updateRemainTime(itemNo) {
			response = await getRemainTime(itemNo);
			let { remainTime } = response;
			const { type } = response;
	    	
	    	let inner = async function() {
	    		if (remainTime <= 0) {
	    			if (type === "종료") {
	    				return;
	    			} else if (await waitForStatus(itemNo, "판매중")) {
	    				remainTime = (await getRemainTime(itemNo)).remainTime;
	    				priceInputSection.hidden = false;
	    			}
	    		}
	
	            document.getElementById("remain-time").innerText = formatTimeText(remainTime);
	            remainTime--;
	        };
	        
	        return inner;
	    }
		
		function formatTimeText(remainTime) {
			let days = Math.floor(remainTime / 86400);
	        let hours = Math.floor((remainTime % 86400) / 3600);
	        let minutes = Math.floor((remainTime % 3600) / 60);
	        let seconds = remainTime % 60;
	
	        let timeText = "";
	        if (days > 0) timeText += days + "일 ";
	        if (hours > 0) timeText += hours + "시간 ";
	        if (minutes > 0) timeText += minutes + "분 ";
	        if (seconds > 0) timeText += seconds + "초";
	        
	        return timeText;
		}
	
		async function waitForStatus(itemNo, targetStatus, maxAttempts = 10, interval = 500) {
		    if (status === targetStatus) {
		        return true;
		    }
		    
		    for (let attempt = 0; attempt < maxAttempts; attempt++) {
		        try {
		            const response = await fetch(`/item/detail/${itemNo}/status`);
		            const currentStatus = await response.text();
		            
		            status = currentStatus;
		            
		            if (currentStatus === targetStatus) {
		                return true;
		            }
		            
		            await new Promise(resolve => setTimeout(resolve, interval));
		        } catch (error) {
		            console.error("waitForStatus(itemNo, targetStatus): " + error);
		        }
		    }
		    
		    return false;
		}
	</script>
</body>
</html>