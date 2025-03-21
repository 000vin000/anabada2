package kr.co.anabada.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.repository.ItemRepository;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class ChatService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    public Chat_Room createChatRoom(Integer itemNo, Integer buyerNo) {
        // 아이템 & 유저 조회
        Item item = itemRepository.findById(itemNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 없습니다."));
        User buyer = userRepository.findById(buyerNo)
                .orElseThrow(() -> new IllegalArgumentException("구매자 정보가 없습니다."));
        
        // 아이템에서 셀러 정보 가져오기
        Seller seller = item.getSeller(); 
        User sellerUser = seller.getUser(); 

        // 채팅방 생성
        Chat_Room chatRoom = new Chat_Room();        
        chatRoom.setItem(item);
        chatRoom.setBuyer(buyer);
        chatRoom.setSeller(sellerUser); 

        return chatRoomRepository.save(chatRoom);
    }

    public Chat_Message saveMessage(Integer roomNo, Integer senderNo, String content) {
        // 채팅방 & 유저 조회
        Chat_Room chatRoom = chatRoomRepository.findById(roomNo)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 없습니다."));
        User sender = userRepository.findById(senderNo)
                .orElseThrow(() -> new IllegalArgumentException("보낸 사람 정보가 없습니다."));

        // 메시지 저장
        Chat_Message message = new Chat_Message();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setMsgContent(content);
        message.setMsgIsRead(false);

        return chatMessageRepository.save(message);
    }
}
