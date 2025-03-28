<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="isLoggedIn" value="${not empty user}" />
<c:set var="userNo" value="${user != null ? user.userNo : -1}" />
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
         <button onclick="createChatRoom(${item.itemNo}, ${item.sellerNo})" id="qnaList">문의하기</button>
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
                     ${(item.itemSaleStartDate != null) ? item.getFormattedDate(item.itemSaleStartDate).concat(" ~ ") : "" }
                     ${(item.itemSaleEndDate != null) ? item.getFormattedDate(item.itemSaleEndDate) : "무기한" }
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

	<div class="body-container">
		<section id="NameSection">
			<div>
				<h1 class="item-name">${item.itemTitle}</h1>
				<button id="favor-btn" data-item-no="${item.itemNo}">☆</button>
				<button id="btnEdit" hidden="hidden">수정</button>
				<button id="btnDelete" hidden="hidden">삭제</button>
			</div>
			<div>
				<button id="chatList">문의하기</button>
			</div>
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

  	<jsp:include page="../footer.jsp"/>

</body>

<script src="/js/recent/config.js"></script>
<script src="/js/recent/addRecent.js"></script>
<script src="/js/recent/getRecent_sidebar.js"></script>
<script src="/js/favor/favorItem.js"></script>
<script src="/js/chat/chatRoomList.js"></script>

<script>

   // Pass isLoggedIn to JavaScript
   var isLoggedIn = ${isLoggedIn};

   document.addEventListener("DOMContentLoaded", function() {

      function createChatRoom(itemNo, sellerNo) {
         if (!isLoggedIn) {
            Swal.fire('로그인 후 채팅을 시작할 수 있습니다.');
            return;
         }

         console.log("Creating chat room...");
         const buyerNo = ${userNo}; 
         const itemTitle = "${item.itemTitle}";

         // 채팅방을 생성하기 위해 API 호출
         fetch(`/chat/createRoom`, {
            method: "POST",
            headers: {
               "Content-Type": "application/json"
            },
            body: JSON.stringify({
               sellerId: sellerNo,
               buyerId: buyerNo,
               itemTitle: itemTitle,
               itemNo: itemNo
            })
         })
         .then(response => response.json())
         .then(data => {
            if (data.roomNo) {
               Swal.fire('채팅방이 생성되었습니다!');
               window.location.href = "/chat/getMessages/" + data.roomNo;
            } else {
               Swal.fire('채팅방 생성 실패', data.message, 'error');
            }
         })
         .catch(error => {
            console.error("채팅방 생성 실패", error);
            Swal.fire('채팅방 생성 실패', '오류가 발생했습니다.', 'error');
         });
      }

      // '문의하기' 버튼 클릭 시 createChatRoom 함수 호출
      const createChatButton = document.getElementById("qnaList");
      if (createChatButton) {
          createChatButton.addEventListener("click", function() {
              const itemNo = ${item.itemNo};  // itemNo 가져오기
              const sellerNo = ${item.sellerNo};  // sellerNo 가져오기
              createChatRoom(itemNo, sellerNo);
          });
      }
   });

	function addCommas(num) {
	    if (isNaN(num)) {
	        return num;
	    }
	
	    num = Number(num).toString();
	    return num.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}

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
</html>

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

	document.addEventListener("DOMContentLoaded", async function() {
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
        if (!/^\d$/.test(e.key) && e.key !== "Backspace" && e.key !== "Tab") {
            e.preventDefault();
        }
    });

    textPrice.addEventListener("input", function() {
        this.value = this.value.replace(/[^\d]/g, "");
    });

    btnBid.addEventListener("click", function() {
        const price = textPrice.value;
        
        if (!isLoggedIn) {
         	Swal.fire("오류", "로그인이 필요한 서비스입니다.", "error");
            //login url
            return;
        }
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
            console.error("updateStatus(itemNo): ", error);
        }
    }
	
	async function initRemainTime(itemNo, type) {
		const response = await fetch(`/item/detail/${itemNo}/remainTime/` + type);
        const data = await response.json();
        remainTime = data;
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

