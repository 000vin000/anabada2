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
    private UserRepository userRepository;  // UserRepository 추가

    public Chat_Room createChatRoom(Integer sellerId, Integer buyerId, String itemTitle, Integer itemNo) {
        // sellerId와 buyerId로 User 객체를 찾기
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));
        User buyer = userRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("Buyer not found"));

        // Chat_Room 객체 생성
        Chat_Room chatRoom = Chat_Room.builder()
            .seller(seller)  // User 객체를 직접 넣음
            .buyer(buyer)    // User 객체를 직접 넣음
            .itemTitle(itemTitle)
            .itemNo(itemNo)
            .build();

        // 채팅방 저장
        return chatRoomRepository.save(chatRoom);
    }
}
