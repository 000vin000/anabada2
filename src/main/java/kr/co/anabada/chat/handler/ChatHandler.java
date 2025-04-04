package kr.co.anabada.chat.handler;

import java.io.IOException;
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

    // 채팅방별 세션 관리 (roomNo -> (userNo -> session))
    private final Map<String, Map<Integer, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            String roomNo = String.valueOf(session.getAttributes().get("roomNo"));
            Integer userNo = (Integer) session.getAttributes().get("userNo");

            if (roomNo == null || userNo == null) {
                session.close(CloseStatus.BAD_DATA);
                return;
            }

            // 세션을 roomSessions에 추가
            roomSessions.computeIfAbsent(roomNo, k -> new ConcurrentHashMap<>()).put(userNo, session);
            System.out.println("User No: " + userNo + " joined Room No: " + roomNo);

            // 기존 채팅 메시지 불러오기
            List<ChatMessageDTO> chatMessages = chatMessageService.getMessagesByRoomNo(Integer.parseInt(roomNo));
            if (chatMessages != null) {
                for (ChatMessageDTO message : chatMessages) {
                    session.sendMessage(new TextMessage(message.getMsgContent()));
                }
            } else {
                System.out.println("채팅 메시지 불러오기 실패: " + roomNo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomNo = String.valueOf(session.getAttributes().get("roomNo"));
        Integer userNo = (Integer) session.getAttributes().get("userNo");

        if (roomNo == null || userNo == null) {
            System.out.println("Error: roomNo or userNo is null");
            return;
        }

        // ✅ WebSocket 메시지 처리
        try {
            ChatMessageDTO chatMessageDTO = chatMessageService.sendMessage(
                Integer.parseInt(roomNo),  
                message.getPayload(),      
                userNo                     
            );

            Map<Integer, WebSocketSession> roomSessionMap = roomSessions.get(roomNo);

            if (roomSessionMap != null) {
                for (Map.Entry<Integer, WebSocketSession> entry : roomSessionMap.entrySet()) {
                    WebSocketSession receiverSession = entry.getValue();
                    Integer receiverUserNo = entry.getKey();

                    if (!receiverUserNo.equals(userNo) && receiverSession.isOpen()) {
                        try {
                            receiverSession.sendMessage(new TextMessage(chatMessageDTO.getMsgContent()));
                        } catch (IOException e) {
                            System.out.println("메시지 전송 실패: " + receiverUserNo);
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("채팅방 세션 없음: " + roomNo);
            }
        } catch (Exception e) {
            System.out.println("메시지 처리 실패: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            String roomNo = String.valueOf(session.getAttributes().get("roomNo"));
            Integer userNo = (Integer) session.getAttributes().get("userNo");

            if (roomNo != null && userNo != null) {
                Map<Integer, WebSocketSession> roomSessionMap = roomSessions.get(roomNo);
                if (roomSessionMap != null) {
                    roomSessionMap.remove(userNo);

                    TextMessage leaveMessage = new TextMessage("상대방이 채팅방에서 퇴장하였습니다.");

                    for (WebSocketSession receiverSession : roomSessionMap.values()) {
                        if (receiverSession.isOpen()) {
                            try {
                                receiverSession.sendMessage(leaveMessage);
                            } catch (IOException e) {
                                System.out.println("퇴장 메시지 전송 실패: " + receiverSession);
                            }
                        }
                    }

                    // 채팅방에 아무도 없으면 제거
                    if (roomSessionMap.isEmpty()) {
                        roomSessions.remove(roomNo);
                    }
                }
            }

            System.out.println("User No: " + userNo + " disconnected from Room No: " + roomNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
