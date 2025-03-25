<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
         <button onclick="openWindow('ChatWindow', '/chat/chatList')" id="qnaList">문의하기</button> <!-- Create chat room button -->
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
   document.addEventListener("DOMContentLoaded", function() {
      // createChatRoom 함수 정의
      function createChatRoom(itemNo, sellerNo) {
    if (!${isLoggedIn}) {
        Swal.fire('로그인 후 채팅을 시작할 수 있습니다.');
        return;
    }

    console.log("Creating chat room...");
    const buyerNo = ${userNo}; // 로그인한 사용자의 ID
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
            Swal.fire('채팅방이 생성되었습니다!')
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
</script>
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
    
    textPrice.addEventListener("keypress", function (e) {
        if (e.key === "Enter") {
            btnBid.click();
        }
    });
   
</script>
</html>
