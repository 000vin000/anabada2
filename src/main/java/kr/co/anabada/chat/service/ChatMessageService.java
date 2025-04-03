package kr.co.anabada.chat.service; 
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
public class ChatMessageService {
	@Autowired
    private ChatMessageRepository chatMessageRepository;
    
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	
	@Autowired
	private UserRepository userRepository;
   
    
 // 메시지 저장 메서드 추가
    public Chat_Message saveMessage(Chat_Message message) {
        return chatMessageRepository.save(message);
    }

    // 메시지 전송 메서드 추가
    public ChatMessageDTO sendMessage(Integer roomNo, String content, Integer senderNo) {
        Chat_Message chatMessage = Chat_Message.builder()
            .chatRoom(chatRoomRepository.findById(roomNo).orElseThrow(() -> new RuntimeException("Chat Room 없음")))
            .msgContent(content)
            .sender(userRepository.findById(senderNo).orElseThrow(() -> new RuntimeException("User 없음")))
            .msgDate(LocalDateTime.now())
            .build();

        chatMessageRepository.save(chatMessage);

        return ChatMessageDTO.builder()
            .roomNo(chatMessage.getRoomNo())
            .msgContent(chatMessage.getMsgContent())
            .senderNo(senderNo)
            .msgDate(chatMessage.getMsgDate())
            .build();
    }



    // 채팅 메시지 가져옴
    public List<ChatMessageDTO> getMessagesByRoomNo(Integer roomNo) {
       
        List<Chat_Message> chatMessages = chatMessageRepository.findByChatRoom_RoomNoOrderByMsgDateAsc(roomNo);

        // 엔티티를 DTO로 변환
        return chatMessages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 엔티티 -> DTO 변환 
    private ChatMessageDTO convertToDTO(Chat_Message message) {
        return ChatMessageDTO.builder()
                .msgNo(message.getMsgNo())
                .msgContent(message.getMsgContent())
                .msgDate(message.getMsgDate())
                .senderNo(message.getSender().getUserNo())  // senderNo 수정
                .build();
    }
}
