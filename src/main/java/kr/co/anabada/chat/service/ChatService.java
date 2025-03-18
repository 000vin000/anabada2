package kr.co.anabada.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.user.entity.User;

@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Transactional
    public Chat_Message saveMessage(Integer roomNo, String content, User sender) {
        Chat_Room chatRoom = chatRoomRepository.findById(roomNo)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        Chat_Message message = new Chat_Message();
        message.setChatRoom(chatRoom);
        message.setMsgContent(content);
        message.setSender(sender);

        return chatMessageRepository.save(message);
    }
    
    
}
