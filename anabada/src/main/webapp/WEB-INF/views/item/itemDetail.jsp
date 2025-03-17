<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>${item.itemName}</title>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
	<div class="body-container">
		<section id="NameSection">
			<div>
				<h1 class="item-name">${item.itemName}</h1>
				<button id="favor-btn" data-item-id="${item.itemNo}">☆</button>
			</div>
			<button onclick="openWindow('QnaWindow', '/item/detail/qna/${item.itemNo}')" id="qnaList">문의하기</button>
		</section>
		
		<section id="QnASection">
			<label id="remainTime"></label>
		</section>
		
		<section id="bidSection">
	        <h2 id="priceHeading">현재가 <label id="currentPrice">${item.addCommas(item.itemPrice)} 원</label></h2>
	        <p id="desiredBidPrice">희망 입찰가
				<input type="text" id="textPrice" disabled="disabled">
				<input type="submit" id="btnBid" value="입찰" disabled="disabled">
			</p>
			<p><button onclick="openWindow('BidlistWindow', '/item/bidList/${item.itemNo}')" id="bidList">입찰기록</button></p>
	    </section>
		
		<section id="detailSection">
		    <table>
		        <tr>
		            <th>판매자</th>
		            <td>${userNick}</td>
		        </tr>
		        <tr>
		            <th>카테고리</th>
		            <td>${item.getCategoryStr(item.itemGender, item.itemCate)}</td>
		        </tr>
		        <tr>
		            <th>경매일자</th>
		            <td>${item.getFormattedDate(item.itemStart)} ~ ${item.getFormattedDate(item.itemEnd)}
		            	( <label id="currentState">상태</label> )</td>
		        </tr>
		        <tr>
		            <th>상품 상태</th>
		            <td>${item.getStatusStr(item.itemStatus)}</td>
		        </tr>
		        <tr>
		            <th>설명</th>
		            <td>${item.itemContent}</td>
		        </tr>
		        <tr>
		            <th>이미지</th>
		            <td>
		                <div class="image-gallery">
		                    <c:forEach var="image" items="${images}">
		                        <img class="item-image" src="data:image/png;base64,${image}" 
		                             style="width: 500px; height: 500px; object-fit: cover; margin: 5px;"/><br>
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
	let intervals = [];
	let currentState = "";
	let remainTime = 0;
	const btnBid = document.getElementById("btnBid");
	const textPrice = document.getElementById("textPrice");

	document.addEventListener('DOMContentLoaded', async function() {
		await updateCurrentState(${item.itemNo});
		let inner = await updateRemainTime(${item.itemNo});
		await inner();
		
		startInterval(() => updateCurrentPrice(${item.itemNo}), 1000);
		startInterval(inner, 1000);
	});
	
	function startInterval(f, s) {
	    let interval = setInterval(f, s);
	    intervals.push(interval);
	}
    
    function stopAllIntervals() {
        intervals.forEach(id => clearInterval(id));
        intervals = [];
    }

    btnBid.addEventListener("click", function() {
        let price = textPrice.value;

        fetch('/item/detail/${item.itemNo}/bid?itemPrice=' + price, { method: "PATCH" })
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
    });
	
    function openWindow(name, url) {
        window.open(
            url, name, 'width=650,height=800,scrollbars=yes'
        );
    }

    function updateCurrentPrice(itemNo) {
        fetch('/item/detail/' + itemNo + '/currentPrice')
            .then(response => response.text())
            .then(data => {
                document.getElementById("currentPrice").innerText = addCommas(data) + " 원";
            })
    }

    async function updateCurrentState(itemNo) {
    	try {
	        let response = await fetch('/item/detail/' + itemNo + '/currentState');
	        let data = await response.text();
	        currentState = data;
	
	        if (data === "판매완료" || data === "종료") {
	            stopAllIntervals();

				      document.getElementById("remainTime").innerText = "";
	            const priceHeading = document.getElementById("priceHeading");
	            const heading = (data === "판매완료") ? "낙찰가: " : "종료가: ";
	            priceHeading.childNodes[0].textContent = heading;
	        }
	    	
	        if (data === "입찰중") {
	            textPrice.disabled = false;
	            btnBid.disabled = false;
	        } else {
	            textPrice.disabled = true;
	            btnBid.disabled = true;
	        }

	        document.getElementById("currentState").innerText = data;
    	} catch (error) {
            console.error('updateCurrentState(itemNo): ', error);
        }
    }
	
	async function initRemainTime(itemNo, type) {
		const response = await fetch('/item/detail/' + itemNo + '/remainTime/' + type);
        const data = await response.json();
        remainTime = data;
	}

	async function updateRemainTime(itemNo) {
    	let type = (currentState === "대기중") ? "start" : "end";
    	await initRemainTime(itemNo, type);
    	
    	let inner = async function() {
    		if (remainTime <= 0) {
    			await updateCurrentState(${item.itemNo});
    			
    			if (currentState !== "입찰중") {
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
            if (currentState === "대기중") {
            	timeText = "시작까지 " + timeText;
            } else if (currentState === "입찰중") {
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