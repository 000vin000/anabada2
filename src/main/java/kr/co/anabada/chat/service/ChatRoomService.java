package kr.co.anabada.chat.service;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.SellerRepository;
import kr.co.anabada.user.repository.UserRepository;

import java.time.LocalDateTime;
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
    public ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;  
    
    @Autowired
    private SellerRepository sellerRepository;
    
    @Transactional
    public Chat_Room createChatRoom(Integer sellerId, Integer buyerId, String itemTitle, Integer itemNo) {
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

    @Transactional
    public void saveMessage(Chat_Message message) {
        chatMessageRepository.save(message);
    }

    public Optional<Chat_Room> getChatRoomByRoomNo(Integer roomNo) {
        return chatRoomRepository.findById(roomNo);
    }

    public List<Chat_Room> getChatRoomsByUser(Integer userId) {
        return chatRoomRepository.findBySeller_User_UserNoOrBuyer_UserNo(userId, userId);
    }
}
