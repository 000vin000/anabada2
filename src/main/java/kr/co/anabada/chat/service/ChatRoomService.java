package kr.co.anabada.chat.service;

import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Chat_Room createChatRoom(User seller, User buyer, String itemTitle, Integer itemNo) {
        Chat_Room chatRoom = Chat_Room.builder()
                .seller(seller)
                .buyer(buyer)
                .itemTitle(itemTitle)
                .itemNo(itemNo)
                .build();
        return chatRoomRepository.save(chatRoom);
    }
}
