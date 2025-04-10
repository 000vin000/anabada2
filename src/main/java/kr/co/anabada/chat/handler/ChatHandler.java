package kr.co.anabada.chat.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.anabada.chat.config.ChatRoomManager;
import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.chat.service.ChatMessageService;
import kr.co.anabada.user.repository.UserRepository;

@Component
public class ChatHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(ChatHandler.class);

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            String uri = session.getUri().toString();
            String roomNoParam = UriComponentsBuilder.fromUriString(uri).build().getQueryParams().getFirst("roomNo");
            String userNoParam = UriComponentsBuilder.fromUriString(uri).build().getQueryParams().getFirst("userNo");

            if (roomNoParam == null || userNoParam == null) {
                session.close(CloseStatus.BAD_DATA);
                return;
            }

            Integer roomNo = Integer.parseInt(roomNoParam);
            Integer userNo = Integer.parseInt(userNoParam);

            session.getAttributes().put("roomNo", roomNo);
            session.getAttributes().put("userNo", userNo);

            ChatRoomManager.addSession(roomNo, session);

            // 이전 메시지 전송
            List<ChatMessageDTO> chatMessages = chatMessageService.getMessagesByRoomNo(roomNo);
            for (ChatMessageDTO message : chatMessages) {
                String jsonMessage = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(jsonMessage));
            }

        } catch (Exception e) {
            log.error("afterConnectionEstablished 예외 발생", e);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();

        JsonNode jsonNode = objectMapper.readTree(payload);

        Integer roomNo = jsonNode.get("roomNo").asInt();
        String content = jsonNode.get("msgContent").asText();
        Integer senderNo = jsonNode.get("senderNo").asInt();

        // 메시지 저장
        Chat_Message savedMessage = chatMessageService.saveMessage(
            Chat_Message.builder()
                .chatRoom(chatRoomRepository.findById(roomNo).orElseThrow())
                .msgContent(content)
                .sender(userRepository.findById(senderNo).orElseThrow())
                .msgDate(LocalDateTime.now())
                .msgIsRead(false)
                .build()
        );

        ChatMessageDTO response = ChatMessageDTO.builder()
            .msgNo(savedMessage.getMsgNo())
            .roomNo(roomNo)
            .msgContent(content)
            .senderNo(senderNo)
            .userNick(savedMessage.getSender().getUserNick())
            .formattedMsgDate(savedMessage.getMsgDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
            .messageType("NEW")
            .build();

        String responseText = objectMapper.writeValueAsString(response);

        // 같은 roomNo를 가진 모든 세션에게 전송
        List<WebSocketSession> sessions = ChatRoomManager.getSessions(roomNo);
        for (WebSocketSession receiverSession : sessions) {
            Object receiverRoomAttr = receiverSession.getAttributes().get("roomNo");

            if (receiverRoomAttr != null && receiverSession.isOpen()) {
                Integer receiverRoomNo = Integer.parseInt(receiverRoomAttr.toString());

                if (receiverRoomNo.equals(roomNo)) {
                    try {
                        receiverSession.sendMessage(new TextMessage(responseText));
                    } catch (IOException e) {
                        log.warn("메시지 전송 실패 - 세션이 닫혔을 수 있음: {}", e.getMessage());
                        ChatRoomManager.removeSession(roomNo, receiverSession);
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            Object roomAttr = session.getAttributes().get("roomNo");

            if (roomAttr == null) return;

            Integer roomNo = Integer.parseInt(roomAttr.toString());

            ChatRoomManager.removeSession(roomNo, session);

            // 남은 세션에 퇴장 메시지 전송
            List<WebSocketSession> remainingSessions = ChatRoomManager.getSessions(roomNo);
            TextMessage leaveMessage = new TextMessage("상대방이 채팅방에서 퇴장하였습니다.");
            for (WebSocketSession receiverSession : remainingSessions) {
                if (receiverSession.isOpen()) {
                    try {
                        receiverSession.sendMessage(leaveMessage);
                    } catch (IOException e) {
                        log.warn("퇴장 메시지 전송 실패: {}", e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            log.error("afterConnectionClosed 예외 발생", e);
        }
    }
}
