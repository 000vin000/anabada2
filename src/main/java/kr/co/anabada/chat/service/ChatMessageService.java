package kr.co.anabada.chat.service;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public void sendMessage(Integer roomId, String msgContent, User sender) {
        // 채팅방을 찾는다.
        Chat_Room chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Chat Room not found"));

        // 새 메시지를 생성한다.
        Chat_Message chatMessage = Chat_Message.builder()
            .msgContent(msgContent)
            .msgDate(LocalDateTime.now())
            .msgIsRead(false) // 기본값으로 읽지 않음
            .chatRoom(chatRoom) // 채팅방 설정
            .sender(sender) // 발신자 설정
            .build();

        // 메시지를 저장한다.
        chatMessageRepository.save(chatMessage);
        
    }
    // saveMessage 메서드 추가
    public void saveMessage(Chat_Message message) {
        chatMessageRepository.save(message);  // Chat_Message 객체를 DB에 저장
    }
    
    

    public List<Chat_Message> getMessagesByRoomId(Integer roomId) {
        // roomId가 Integer로 처리됨
        return chatMessageRepository.findByChatRoom_Id(roomId);
    }
}
