package kr.co.anabada.chat.service;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void sendMessage(Integer roomNo, String msgContent, Integer senderNo) {
        // 채팅방을 roomNo로 찾기
        Chat_Room chatRoom = chatRoomRepository.findById(roomNo).orElseThrow(() -> new RuntimeException("Chat room not found"));
        
        // 메시지 객체 생성
        Chat_Message chatMessage = Chat_Message.builder()
                .chatRoom(chatRoom) // 채팅방 설정
                .msgContent(msgContent)
                .msgDate(LocalDateTime.now())
                .msgIsRead(false)
                .sender(userRepository.findById(senderNo).orElseThrow(() -> new RuntimeException("Sender not found"))) // 송신자 설정
                .build();

        // 메시지 저장
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
