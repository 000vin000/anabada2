<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<h2>채팅 목록</h2>

<c:choose>
    <c:when test="${empty chatRooms}">
        <p>채팅방이 없습니다.</p>
        <button class="btn btn-primary" id="createChatBtn"
            data-seller-id="${sellerId}" 
            data-item-title="${itemTitle}" 
            data-item-no="${itemNo}">
            채팅 생성
        </button>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="chatRoom" items="${chatRooms}">
                <li>
                    <a href="<spring:url value='/chat/getMessages/${chatRoom.roomNo}'/>">${chatRoom.itemTitle}</a>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const createChatBtn = document.getElementById("createChatBtn");
        
        if (createChatBtn) {
            createChatBtn.onclick = function() {
                const buyerId = ${userNo};  // 현재 로그인된 사용자 ID
                const sellerId = this.getAttribute("data-seller-id");
                const itemTitle = this.getAttribute("data-item-title");
                const itemNo = this.getAttribute("data-item-no");

                if (!sellerId || !itemTitle || !itemNo) {
                    alert("상품 정보를 가져오는 데 문제가 발생했습니다.");
                    return;
                }

                // JSON 형식으로 채팅방 생성 요청
                fetch("/chat/createRoom", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        sellerId: sellerId,
                        buyerId: buyerId,
                        itemTitle: itemTitle,
                        itemNo: itemNo
                    })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.status === "success") {
                        alert("채팅방이 생성되었습니다!");
                        window.location.href = "/chat/getMessages/" + data.chatRoom.roomNo;
                    } else {
                        alert("채팅방 생성 실패: " + data.message);
                    }
                })
                .catch(error => {
                    alert("채팅방 생성 중 오류 발생");
                    console.error(error);
                });
            };
        }
    });
</script>
