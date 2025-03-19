package kr.co.anabada.chat.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // RoomNo를 URL에서 추출하여 세션에 저장
        String roomNo = (String) session.getUri().getPath().split("/")[2];
        String userId = session.getId(); // 세션 ID를 사용자 ID로 사용
        session.getAttributes().put("roomNo", roomNo);
        sessions.put(userId, session); // 사용자 ID를 키로 사용하여 세션 저장
        System.out.println("User ID: " + userId + " connected to Room No: " + roomNo);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomNo = (String) session.getAttributes().get("roomNo");

        if (roomNo == null) {
            System.out.println("Error: roomNo is null");
            return;
        }

        // 모든 세션 중 같은 roomNo를 가진 사용자에게 메시지 전송
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            WebSocketSession receiverSession = entry.getValue();
            String receiverRoomNo = (String) receiverSession.getAttributes().get("roomNo");

            // 자신에게는 메시지를 보내지 않음
            if (receiverSession.isOpen() && !receiverSession.getId().equals(session.getId()) && receiverRoomNo.equals(roomNo)) {
                receiverSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = session.getId(); // 세션 ID를 사용자 ID로 사용
        sessions.remove(userId);
        System.out.println("User ID: " + userId + " has been disconnected.");
        
        String roomNo = (String) session.getAttributes().get("roomNo");
        if (roomNo != null) {
            TextMessage leaveMessage = new TextMessage("상대방이 채팅방에서 퇴장하였습니다.");
            for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
                WebSocketSession receiverSession = entry.getValue();
                String receiverRoomNo = (String) receiverSession.getAttributes().get("roomNo");
                if (receiverRoomNo.equals(roomNo)) {
                    receiverSession.sendMessage(leaveMessage);
                }
            }
        }
    }
}
