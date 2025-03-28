package kr.co.anabada.chat.service;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.SellerRepository;
import kr.co.anabada.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    // 특정 구매자가 특정 상품에 대해 이미 채팅방이 있는지 확인
    public boolean existsByRoomNo(Integer roomNo) {
        return chatRoomRepository.findByRoomNo(roomNo).isPresent();  
    }

    // 채팅방 조회 (buyerId와 itemNo 기준으로)
    public boolean existsByBuyerAndItem(Integer buyerId, Integer itemNo) {
        return chatRoomRepository.existsByBuyer_UserNoAndItemNo(buyerId, itemNo);
    }
    
    public Optional<Chat_Room> getChatRoomByBuyerAndItem(Integer buyerUserNo, Integer itemNo) {
        return chatRoomRepository.findByBuyer_UserNoAndItemNo(buyerUserNo, itemNo);
    }


    // 채팅방 생성 (중복 방지)
    @Transactional
    public Chat_Room createChatRoom(Integer sellerId, Integer buyerId, String itemTitle, Integer itemNo) {
        Optional<Chat_Room> existingChatRoom = getChatRoomByBuyerAndItem(buyerId, itemNo);
        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get(); // 이미 존재하는 채팅방 반환
        }

        Seller seller = sellerRepository.findById(sellerId)
            .orElseThrow(() -> new RuntimeException("판매자 정보를 찾을 수 없습니다."));
        User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new RuntimeException("구매자 정보를 찾을 수 없습니다."));

        Chat_Room chatRoom = Chat_Room.builder()
            .seller(seller)
            .buyer(buyer)
            .itemTitle(itemTitle)
            .itemNo(itemNo)
            .build();

        return chatRoomRepository.save(chatRoom);
    }

    // 메시지 저장
    @Transactional
    public void saveMessage(Chat_Message message) {
        chatMessageRepository.save(message);
    }

    // 채팅방 조회 (roomNo 기준)
    public Optional<Chat_Room> getChatRoomByRoomNo(Integer roomNo) {
        return chatRoomRepository.findById(roomNo);
    }

    // 특정 사용자의 채팅방 목록 조회
    public List<Chat_Room> getChatRoomsByUser(Integer userId) {
        return chatRoomRepository.findBySeller_User_UserNoOrBuyer_UserNo(userId, userId);
    }
}
