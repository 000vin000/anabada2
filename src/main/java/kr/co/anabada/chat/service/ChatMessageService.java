package kr.co.anabada.chat.service;

import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.dto.ChatRoomDTO;
import kr.co.anabada.chat.dto.SenderDTO;
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
import java.util.stream.Collectors;

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
        Chat_Room chatRoom = chatRoomRepository.findByRoomNo(roomNo).orElseThrow(() -> new RuntimeException("Chat room 찾을 수 없음"));
        
        // 메시지 객체 생성
        Chat_Message chatMessage = Chat_Message.builder()
                .chatRoom(chatRoom) // 채팅방 설정
                .msgContent(msgContent)
                .msgDate(LocalDateTime.now())
                .msgIsRead(false)
                .sender(userRepository.findById(senderNo).orElseThrow(() -> new RuntimeException("Sender 찾을 수 없음"))) 
                .build();

        // 메시지 저장
        chatMessageRepository.save(chatMessage);
    }

    // saveMessage 메서드 추가
    public void saveMessage(Chat_Message message) {
        chatMessageRepository.save(message);  
    }
    
    // ChatMessage 엔티티를 ChatMessageDTO로 변환하는 메서드
    private ChatMessageDTO convertToDTO(Chat_Message chatMessage) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setMsgNo(chatMessage.getMsgNo());
        dto.setMsgContent(chatMessage.getMsgContent());
        dto.setMsgIsRead(chatMessage.getMsgIsRead());
        dto.setMsgDate(chatMessage.getMsgDate());

        // SenderDTO 설정
        SenderDTO senderDTO = new SenderDTO();
        senderDTO.setUserNo(chatMessage.getSender().getUserNo());
        senderDTO.setUserId(chatMessage.getSender().getUserId());
        senderDTO.setUserNick(chatMessage.getSender().getUserNick());
        dto.setSender(senderDTO);

        // ChatRoomDTO 설정
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setRoomNo(chatMessage.getChatRoom().getRoomNo());
        chatRoomDTO.setItemTitle(chatMessage.getChatRoom().getItemTitle());
        chatRoomDTO.setItemNo(chatMessage.getChatRoom().getItemNo());
        dto.setChatRoom(chatRoomDTO);

        return dto;
    }

    public List<ChatMessageDTO> getMessagesByRoomNo(Integer roomNo) {
        List<Chat_Message> messages = chatMessageRepository.findByChatRoomRoomNo(roomNo);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
