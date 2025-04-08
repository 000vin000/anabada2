package kr.co.anabada.chat.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // 메시지 저장
    public Chat_Message saveMessage(Chat_Message message) {
        return chatMessageRepository.save(message);
    }

    // 메시지 전송
    public ChatMessageDTO sendMessage(Integer roomNo, String content, Integer senderNo) {
        Chat_Message chatMessage = Chat_Message.builder()
                .chatRoom(chatRoomRepository.findById(roomNo).orElseThrow(() -> new RuntimeException("Chat Room 없음")))
                .msgContent(content)
                .sender(userRepository.findById(senderNo).orElseThrow(() -> new RuntimeException("User 없음")))
                .msgDate(LocalDateTime.now())
                .msgIsRead(false)
                .build();

        chatMessageRepository.save(chatMessage);

        return ChatMessageDTO.builder()
                .msgNo(chatMessage.getMsgNo())
                .roomNo(chatMessage.getRoomNo())
                .msgContent(chatMessage.getMsgContent())
                .senderNo(senderNo)
                .userNick(chatMessage.getSender().getUserNick()) // 닉네임 추가
                .msgDate(chatMessage.getMsgDate())
                .formattedMsgDate(chatMessage.getMsgDate().format(formatter)) // 날짜 포맷 추가
                .msgIsRead(false)
                .messageType("NEW")
                .build();
    }

    // 채팅 메시지 조회
    public List<ChatMessageDTO> getMessagesByRoomNo(Integer roomNo) {
        List<Chat_Message> chatMessages = chatMessageRepository.findByChatRoom_RoomNoOrderByMsgDateAsc(roomNo);
        return chatMessages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 엔티티 → DTO 변환
    private ChatMessageDTO convertToDTO(Chat_Message message) {
        return ChatMessageDTO.builder()
                .msgNo(message.getMsgNo())
                .roomNo(message.getRoomNo())
                .msgContent(message.getMsgContent())
                .senderNo(message.getSender().getUserNo())
                .userNick(message.getSender().getUserNick()) 
                .msgDate(message.getMsgDate())
                .formattedMsgDate(message.getMsgDate().format(formatter)) // 포맷된 날짜 포함
                .msgIsRead(message.getMsgIsRead())
                .messageType("HISTORY")
                .build();
    }
}
