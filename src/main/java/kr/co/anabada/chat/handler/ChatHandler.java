package kr.co.anabada.chat.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import kr.co.anabada.chat.service.ChatService;
import kr.co.anabada.user.entity.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomNo = (String) session.getAttributes().get("roomNo");

        if (roomNo == null) {
            System.out.println("Error: roomNo is null");
            return;
        }

        // DB에 메시지 저장
        User sender = (User) session.getAttributes().get("user");
        chatService.saveMessage(Integer.parseInt(roomNo), message.getPayload(), sender);

        // 같은 방에 있는 사용자에게 메시지 전송
        for (WebSocketSession receiverSession : sessions.values()) {
            String receiverRoomNo = (String) receiverSession.getAttributes().get("roomNo");

            if (receiverSession.isOpen() && receiverRoomNo.equals(roomNo)) {
                receiverSession.sendMessage(message);
            }
        }
    }
}