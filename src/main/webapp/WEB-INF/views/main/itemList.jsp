<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<body>
<div id="itemOption">
      <ul>
         <c:if test="${not empty itemList}">
            <c:forEach var="item" items="${itemList}">
               <li>
                  <a href="/item/detail/${item.item_no}" id="card">
                  	<c:if test="${not empty item.base64Image}">
                        <img src="data:image/png;base64,${item.base64Image}" width="100px" height="100px"/>
                    </c:if>
                     <hr id="line">
                     <p id="itemName">${item.item_title}</p> 
                     <hr id="line">
                     <p id="itemPrice">${item.item_price}원 </p>
                     <p id="seller">${item.user_nick}</p>
                     <p id="remainingTime">${item.remainingTime(item.item_sale_end_date)}</p>
                  </a>
               </li>
            </c:forEach>
         </c:if>
         <c:if test="${empty itemList}">
			<div>${error}</div>
		</c:if>
      </ul>
   </div>
</body>
