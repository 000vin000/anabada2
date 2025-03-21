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
        Item item = itemRepository.findByItemNo(itemNo);
                
        User buyer = userRepository.findByBuyerNo(buyerNo);
               
        
        // 아이템에서 셀러 정보 가져오기
        Seller seller = item.getSeller(); 

        // 채팅방 생성
        Chat_Room chatRoom = new Chat_Room();        
        chatRoom.setItem(item);
        chatRoom.setBuyer(buyer);
        chatRoom.setSeller(seller); 

        return chatRoomRepository.saveChatRoom(chatRoom);
    }

    public Chat_Message saveMessage(Integer roomNo, Integer senderNo, String content) {
        // 채팅방 & 유저 조회
        Chat_Room chatRoom = chatRoomRepository.findByRoomNo(roomNo);            
        User sender = userRepository.findBySenderNo(senderNo);

        // 메시지 저장
        Chat_Message message = new Chat_Message();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setMsgContent(content);
        message.setMsgIsRead(false);

        return chatMessageRepository.saveMessage(message);
    }
}
