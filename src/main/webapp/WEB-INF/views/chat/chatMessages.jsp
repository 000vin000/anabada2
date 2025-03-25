<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>채팅 메시지</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
        }
        th {
            text-align: left;
        }
    </style>
</head>
<body>
    <h2>채팅 메시지</h2>
    <table>
        <thead>
            <tr>
                <th>메시지 번호</th>
                <th>내용</th>
                <th>보낸 사람</th>
                <th>채팅방</th>
                <th>날짜</th>
                <th>읽음 여부</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="message" items="${chatMessages}">
                <tr>
                    <td>${message.msgNo}</td>
                    <td>${message.msgContent}</td>
                    <td>${message.sender.userNick} (${message.sender.userId})</td>
                    <td>${message.chatRoom.itemTitle}</td>
                    <td>${message.msgDate}</td>
                    <td>${message.msgIsRead ? '읽음' : '읽지 않음'}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
