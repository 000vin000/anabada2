<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
</head>
<body>
	<div class="body-container">
		<section id="NameSection">
			<div>
				<h1 class="item-name">${item.itemTitle}</h1>
				<button id="favor-btn" data-item-id="${item.itemNo}">☆</button>
				<button id="btnEdit" hidden="hidden">수정</button>
				<button id="btnDelete" hidden="hidden">삭제</button>
			</div>
			<button onclick="openWindow('QnaWindow', '/')" id="qnaList">문의하기</button>
		</section>
		
		<section id="QnASection">
			<label id="remainTime"></label>
		</section>
		
		<section id="bidSection">
	        <h2 id="priceHeading">현재가 <label id="price">${item.addCommas(item.itemPrice)} 원</label></h2>
	        <p id="desiredBidPrice">희망 입찰가
	        	<input type="number" id="textPrice" min="0" step="100">
				<input type="submit" id="btnBid" value="입찰">
			</p>
			<p><button onclick="openWindow('BidlistWindow', '/')" id="bidList">입찰기록</button></p>
	    </section>
		
		<section id="detailSection">
		    <table>
		        <tr>
		            <th>판매자</th>
		            <td>${item.sellerNick}</td>
		        </tr>
		        <tr>
		            <th>카테고리</th>
		            <td>${item.categoryName}</td>
		        </tr>
		        <tr>
		            <th>경매일자</th>
		            <td>
		            	${(item.itemSaleStartDate != null) ? item.getFormattedDate(item.itemSaleStartDate).concat(" ~ ") : ""}
		            	${(item.itemSaleEndDate != null) ? item.getFormattedDate(item.itemSaleEndDate) : "무기한"}
		            	( <label id="status">${item.itemStatus}</label> )
		            </td>
		        </tr>
		        <c:if test="${item.itemQuality != null}">
		        <tr>
		            <th>품질</th>
		            <td>${item.itemQuality}</td>
		        </tr>
		        </c:if>
		        <tr>
		            <th>설명</th>
		            <td>${item.itemContent}</td>
		        </tr>
		        <tr>
		            <th>이미지</th>
		            <td>
		            	<div>
						    <c:forEach begin="0" end="${item.imageCount-1}" var="index">
						        <img src="/image/${item.itemNo}/${index}" alt="물품 이미지 ${index+1}">
						    </c:forEach>
					    </div>
		            </td>
		        </tr>
		    </table>
		</section>
	    
    	<button onclick="window.history.back()" class="toMypage">뒤로가기</button>
    </div>
    <jsp:include page="../sidebar.jsp" />
  	<jsp:include page="../footer.jsp"/>
</body>
<script src="/js/todaypick.js"></script>
<script>
	document.addEventListener("DOMContentLoaded", () => {
		const itemNo = window.location.pathname.split('/').pop(); // 상품상세페이지로 갔을때 넘버 가져오기
		// /로 배열을 나누고 마지막 요소를 itemNo에 저장함
		const itemName = document.querySelector(".item-name").innerText;
		// 이름을 가져오려고 이너텍스트를 훔쳐오는 부분
		const itemImage = document.querySelector(".item-image")?.src || "";
		// 페이지에서 이미지 클래스를 찾아서 첫번째 이미지를 가져와서 저장함 실패하면 비어있도록 수정
		
		if (itemNo) {
			addRecentView(itemNo, itemName, itemImage);
		}
	}); // jhu
</script>
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
	document.addEventListener("DOMContentLoaded", function () {
	    const favBtn = document.getElementById("favor-btn");
	    const itemNo = favBtn.dataset.itemNo;
	    const favImg = document.createElement("img");
	    favImg.alt = "☆";
	   	favBtn.innerHTML = "";
	    favBtn.appendChild(favImg);
	    
	    function updateFavoriteUI(isFavorite) {
			favImg.src = isFavorite ? "/images/favor-star-filled.png" : "/images/favor-star-empty.png";
	    }
		
    	fetch(`/api/favor/${itemNo}`)
    		.then(res => res.json())
       		.then(data => {
       			updateFavoriteUI(data.isFavorite);
       	})
	    
	    async function toggleFavorite() {
	        const response = await fetch(`/api/favor/${itemNo}`, { method: "POST" });
	        if (response.status === 401) {
	            alert("로그인이 필요합니다.");
	        } else {
		        const isFavorited = await response.json();
		        updateFavoriteUI(isFavorited);
	        }
	    }
	
	    favBtn.addEventListener("click", toggleFavorite);
	}); // jhu
</script>
<script>
	const isLoggedIn = ${isLoggedIn};
	const userNo = ${userNo};
	const itemNo = ${item.itemNo};
	const sellerNo = ${item.sellerNo};
	const btnEdit = document.getElementById("btnEdit");
	const btnDelete = document.getElementById("btnDelete");
	const btnBid = document.getElementById("btnBid");
	const textPrice = document.getElementById("textPrice");
	let intervals = [];
	let status = "";
	let remainTime = 0;

	document.addEventListener('DOMContentLoaded', async function() {
		if(isLoggedIn) {
			initIfOwner(userNo, sellerNo);
		}
		updateStatus(itemNo);
		//itemDetail 페이지 타입별 분리시, itemSaleType 검사하는 if 조건 제거
		if(`${item.itemSaleType}` === "경매" && `${item.itemSaleEndDate}` != null) {
			await updateStatus(itemNo);
			let inner = await updateRemainTime(itemNo);
			await inner();
			console.log(remainTime);
			
			startInterval(() => updatePrice(itemNo), 1000);
			startInterval(inner, 1000);
		}
	});
	
	function initIfOwner(userNo, sellerNo) {
		if (userNo === sellerNo) {
            textPrice.disabled = true;
            btnBid.disabled = true;
            btnEdit.hidden = false;
            btnDelete.hidden = false;
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
    
    textPrice.addEventListener("keypress", function(e) {
        if (!/^\d$/.test(e.key) && e.key !== 'Backspace' && e.key !== 'Tab') {
            e.preventDefault();
        }
    });

    textPrice.addEventListener("input", function() {
        this.value = this.value.replace(/[^\d]/g, '');
    });

    btnBid.addEventListener("click", function() {
        const price = textPrice.value;
        
        if (${empty user}) {
            alert("로그인이 필요한 서비스입니다.");
            //login url
            return;
        }
        if (!price.trim() || isNaN(Number(price))) {
            return;
        }
        if (${empty item.pointBalance}) {
        	if (confirm("포인트 계정이 없습니다.\n포인트 페이지로 이동하시겠습니까?")) {
				//point_account url
        	}
        	return;
        }
        
        const pointBalance = parseFloat("${item.pointBalance}");
        if (pointBalance )
        if (pointBalance < price) {
            alert("포인트 잔액이 부족합니다.");
            return;
        }
        if (confirm("현재 포인트 잔액은 " + pointBalance + "원 입니다.\n" + price + "원으로 입찰하시겠습니까?")) {
        	fetch(`/item/detail/${itemNo}/bid`, {
                method: "PATCH",
                headers: {
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
                alert(data);
            })
            .catch(error => {
                alert(error.message);
            });
        } else {
        	if (confirm("포인트 페이지로 이동하시겠습니까?")) {
        		//point_account url
        	}
        }
    });
	
    function openWindow(name, url) {
        window.open(
            url, name, 'width=650,height=800,scrollbars=yes'
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

				document.getElementById("remainTime").innerText = "";
	            const priceHeading = document.getElementById("priceHeading");
	            const heading = (data === "예약중" || data === "판매완료") ? "낙찰가: " : "종료가: ";
	            priceHeading.childNodes[0].textContent = heading;
	        }
	    	
	        if (data != "판매중") {
	            textPrice.disabled = true;
	            btnBid.disabled = true;
	        }

	        document.getElementById("status").innerText = data;
    	} catch (error) {
            console.error('updateStatus(itemNo): ', error);
        }
    }
	
	async function initRemainTime(itemNo, type) {
		const response = await fetch(`/item/detail/${itemNo}/remainTime/` + type);
        const data = await response.json();
        remainTime = data;
        console.log(remainTime);
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

            let timeText = "남은 시간 : ";
            if (status === "대기중") {
            	timeText = "시작까지 " + timeText;
            } else if (status === "판매중") {
            	timeText = "종료까지 " + timeText;
            }
            
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

            document.getElementById("remainTime").innerText = timeText;
            remainTime--;
        };
        
        return inner;
    }
</script>
</html>