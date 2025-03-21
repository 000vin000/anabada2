package kr.co.anabada.chat.service;

import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    // 판매자 또는 구매자의 userNo로 채팅방 조회
    public Optional<Chat_Room> getChatRoomByUsers(Integer sellerUserNo, Integer buyerUserNo) {
        return chatRoomRepository.findBySellerUserNoOrBuyerUserNo(sellerUserNo, buyerUserNo);
    }

    // 채팅방 생성 메서드 (테스트용)
    public Chat_Room createChatRoom(User seller, User buyer, Integer itemNo, String itemTitle) {
        Chat_Room chatRoom = Chat_Room.builder()
                .seller(seller)
                .buyer(buyer)
                .itemNo(itemNo)
                .itemTitle(itemTitle)
                .build();
        
        return chatRoomRepository.save(chatRoom);
    }
}
