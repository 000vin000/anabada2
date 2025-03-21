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

    public Chat_Message sendMessage(Integer roomId, User sender, String content) {
        // roomId가 Integer로 수정되었으므로 Integer로 처리
        Chat_Room chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        Chat_Message message = Chat_Message.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .msgContent(content)
                .msgIsRead(false)
                .msgDate(LocalDateTime.now())
                .build();

        return chatMessageRepository.save(message);
    }

    public List<Chat_Message> getMessagesByRoomId(Integer roomId) {
        // roomId가 Integer로 처리됨
        return chatMessageRepository.findByChatRoom_Id(roomId);
    }
}
