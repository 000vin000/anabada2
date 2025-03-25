package kr.co.anabada.chat.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.service.ChatMessageService;

@Component
public class ChatHandler extends TextWebSocketHandler {

    @Autowired
    private ChatMessageService chatMessageService;

    private final Map<String, Map<Integer, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomNo = (String) session.getAttributes().get("roomNo");
        Integer userNo = (Integer) session.getAttributes().get("userNo");

        if (roomNo == null || userNo == null) {
            System.out.println("Error: roomNo or userNo is null");
            return;
        }

        roomSessions.computeIfAbsent(roomNo, k -> new ConcurrentHashMap<>()).put(userNo, session);

        System.out.println("User No: " + userNo + " Room No: " + roomNo);

        List<ChatMessageDTO> chatMessages = chatMessageService.getMessagesByRoomNo(Integer.valueOf(roomNo));
        for (ChatMessageDTO message : chatMessages) {
            session.sendMessage(new TextMessage(message.getMsgContent()));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomNo = (String) session.getAttributes().get("roomNo");
        Integer userNo = (Integer) session.getAttributes().get("userNo");

        if (roomNo == null || userNo == null) {
            System.out.println("Error: roomNo or userNo is null");
            return;
        }

        chatMessageService.sendMessage(Integer.valueOf(roomNo), message.getPayload(), userNo);

        Map<Integer, WebSocketSession> roomSessionMap = roomSessions.get(roomNo);

        if (roomSessionMap != null) {
            for (Map.Entry<Integer, WebSocketSession> entry : roomSessionMap.entrySet()) {
                WebSocketSession receiverSession = entry.getValue();
                Integer receiverUserNo = entry.getKey();

                if (!receiverUserNo.equals(userNo) && receiverSession.isOpen()) {
                    receiverSession.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomNo = (String) session.getAttributes().get("roomNo");
        Integer userNo = (Integer) session.getAttributes().get("userNo");

        if (roomNo != null && userNo != null) {
            Map<Integer, WebSocketSession> roomSessionMap = roomSessions.get(roomNo);
            if (roomSessionMap != null) {
                roomSessionMap.remove(userNo);

                TextMessage leaveMessage = new TextMessage("상대방이 채팅방에서 퇴장하였습니다.");

                for (Map.Entry<Integer, WebSocketSession> entry : roomSessionMap.entrySet()) {
                    WebSocketSession receiverSession = entry.getValue();

                    if (receiverSession.isOpen()) {
                        receiverSession.sendMessage(leaveMessage);
                    }
                }

                if (roomSessionMap.isEmpty()) {
                    roomSessions.remove(roomNo);
                }
            }
        }

        System.out.println("User No: " + userNo + " disconnected from Room No: " + roomNo);
    }
}
