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
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            }

            // 안 읽은 메시지 자동 읽음 처리
            for (ChatMessageDTO message : chatMessages) {
                if (!message.getSenderNo().equals(userNo) && !message.getMsgIsRead()) {
                    // DB에서 읽음 처리
                    chatMessageService.updateMessageReadStatus(message.getMsgNo(), true);

                    // 보낸 사람에게도 알림
                    ChatMessageDTO readUpdate = ChatMessageDTO.builder()
                        .msgNo(message.getMsgNo())
                        .msgIsRead(true)
                        .messageType("READ_UPDATE")
                        .build();

                    String readUpdateText = objectMapper.writeValueAsString(readUpdate);

                    List<WebSocketSession> allSessions = ChatRoomManager.getSessions(roomNo);
                    for (WebSocketSession s : allSessions) {
                        Integer sUserNo = (Integer) s.getAttributes().get("userNo");
                        if (sUserNo.equals(message.getSenderNo()) && s.isOpen()) {
                            s.sendMessage(new TextMessage(readUpdateText));
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("afterConnectionEstablished 예외 발생", e);
        }
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(message.getPayload());

        Integer roomNo = jsonNode.get("roomNo").asInt();
        String content = jsonNode.get("msgContent").asText();
        Integer senderNo = jsonNode.get("senderNo").asInt();

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
            .msgIsRead(false)
            .messageType("NEW")
            .build();

        // 모든 세션에 메시지 전송
        broadcastToRoom(roomNo, response);

        // 읽음 여부 판단 (상대방이 접속해 있는지 확인)
        List<WebSocketSession> sessions = ChatRoomManager.getSessions(roomNo);
        for (WebSocketSession receiverSession : sessions) {
            Integer userNo = (Integer) receiverSession.getAttributes().get("userNo");
            if (!userNo.equals(senderNo) && receiverSession.isOpen()) {
                chatMessageService.updateMessageReadStatus(savedMessage.getMsgNo(), true);
                notifyReadStatusToSender(roomNo, savedMessage.getMsgNo(), senderNo);
                break;
            }
        }
    }

    private void notifyReadStatusToSender(Integer roomNo, Integer msgNo, Integer senderNo) throws IOException {
        List<WebSocketSession> sessions = ChatRoomManager.getSessions(roomNo);
        for (WebSocketSession session : sessions) {
            Integer userNo = (Integer) session.getAttributes().get("userNo");
            if (userNo.equals(senderNo) && session.isOpen()) {
                ChatMessageDTO readAck = ChatMessageDTO.builder()
                    .msgNo(msgNo)
                    .msgIsRead(true)
                    .messageType("READ_UPDATE")
                    .build();
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(readAck)));
            }
        }
    }

    private void broadcastToRoom(Integer roomNo, ChatMessageDTO messageDTO) throws IOException {
        String text = objectMapper.writeValueAsString(messageDTO);
        List<WebSocketSession> sessions = ChatRoomManager.getSessions(roomNo);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(text));
                } catch (IOException e) {
                    log.warn("메시지 전송 실패 - {}", e.getMessage());
                    ChatRoomManager.removeSession(roomNo, session);
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            Integer roomNo = (Integer) session.getAttributes().get("roomNo");
            if (roomNo != null) {
                ChatRoomManager.removeSession(roomNo, session);
            }

            // 나가기 처리
            if (session.getAttributes().containsKey("userLeft")) {
                List<WebSocketSession> remainingSessions = ChatRoomManager.getSessions(roomNo);
                for (WebSocketSession s : remainingSessions) {
                    if (s.isOpen()) {
                        String leaveMsg = "{\"messageType\":\"LEAVE\",\"content\":\"상대방이 채팅방에서 퇴장하였습니다.\"}";
                        s.sendMessage(new TextMessage(leaveMsg));
                    }
                }
            }

        } catch (Exception e) {
            log.error("afterConnectionClosed 예외 발생", e);
        }
    }
}
