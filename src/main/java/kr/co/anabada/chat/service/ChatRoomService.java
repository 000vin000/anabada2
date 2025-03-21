package kr.co.anabada.chat.service;

import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;  

    public Chat_Room createChatRoom(Integer sellerId, Integer buyerId, String itemTitle, Integer itemNo) {
      
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller 찾을 수 없음"));
        User buyer = userRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("Buyer 찾을 수 없음"));

        Chat_Room chatRoom = Chat_Room.builder()
            .seller(seller)  
            .buyer(buyer)    
            .itemTitle(itemTitle)
            .itemNo(itemNo)
            .build();

        return chatRoomRepository.save(chatRoom);
    }
}
