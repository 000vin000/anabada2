package kr.co.anabada.chat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item_Category;
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

    // 채팅방 생성 메소드
    @Transactional
    public Chat_Room createRoom(Item item, User receiverUser) {
        // 아이템 존재 여부 확인
        Item existingItem = itemRepository.findById(item.getItemNo())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));

        // senderNo=1 하드코딩
        User sender = userRepository.findById(1).orElseThrow(() -> 
            new IllegalArgumentException("ID 1인 송신자가 존재하지 않습니다.")
        );

        // receiverNo=2 하드코딩
        User receiver = userRepository.findById(3).orElseThrow(() -> 
            new IllegalArgumentException("ID 3인 수신자가 존재하지 않습니다.")
        );

        // 기존 채팅방 확인
        Chat_Room existingRoom = chatRoomRepository.findByItemAndSenderNoAndReceiverNo(existingItem, sender, receiver);
        if (existingRoom != null) {
            return existingRoom; // 기존 방 반환
        }

        // 새 채팅방 생성
        Chat_Room chatRoom = new Chat_Room();
        chatRoom.setItem(existingItem);
        chatRoom.setSenderNo(sender);
        chatRoom.setReceiverNo(receiver);
        chatRoom.setRoomStatus(Chat_Room.RoomStatus.ACTIVE); // 초기 상태 설정

        // 채팅방 저장
        return chatRoomRepository.save(chatRoom);
    }




    // 채팅 메시지 저장
    @Transactional
    public Chat_Message saveMessage(Integer roomNo, String content, User sender) {
        Chat_Room chatRoom = chatRoomRepository.findById(roomNo)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

       
        User receiver = (chatRoom.getSenderNo().equals(sender)) ? chatRoom.getReceiverNo() : chatRoom.getSenderNo();

        Chat_Message message = new Chat_Message();
        message.setChatRoom(chatRoom);
        message.setMsgContent(content);
        message.setSenderNo(sender);
        message.setReceiverNo(receiver);
        message.setSenderNick(sender.getUserNick());
        message.setReceiverNick(receiver.getUserNick());
        message.setMsgDate(LocalDateTime.now());
        message.setMsgIsRead(false);

        return chatMessageRepository.save(message);
    }

    // roomNo로 채팅방 정보 가져오기
    public Chat_Room getChatRoom(Integer roomNo) {
        return chatRoomRepository.findById(roomNo).orElse(null);
    }

    // roomNo로 채팅 메시지 가져오기
    public List<Chat_Message> getMessagesByRoomNo(Integer roomNo) {
        return chatMessageRepository.findByChatRoomRoomNo(roomNo);
    }

    // 더미 아이템 생성 (DB에 저장 포함)
    public List<Item> createDummyItems() {
        List<Item> dummyItems = new ArrayList<>();

        // 더미 카테고리 생성 및 저장
        Item_Category category = new Item_Category();
        category.setCategoryNo("10100207"); // 카테고리 번호 설정
        // itemCategoryRepository.save(category); // 저장 필요

        // 더미 판매자 생성 및 저장
        Seller seller = new Seller();
        seller.setSellerNo(1); // 판매자 번호 설정
        // sellerRepository.save(seller); // 저장 필요

        // 더미 아이템 생성
        Item item = new Item();
        item.setItemNo(1); // 아이템 번호
        item.setItemTitle("더미 아이템"); // 아이템 제목
        item.setItemPrice(5000L); // 가격 설정
        item.setCategory(category); // 카테고리 설정
        item.setSeller(seller); // 판매자 설정
        item.setItemSaleType(Item.ItemSaleType.AUCTION); // 판매 유형 설정
        item.setItemQuantity(10); // 수량 설정
        item.setItemLatitude(37.5665); // 위도 설정 (예: 서울)
        item.setItemLongitude(126.978); // 경도 설정 (예: 서울)
        item.setItemPurcConfirmed(false); // 구매 확인 설정
        item.setItemSaleConfirmed(false); // 판매 확인 설정

        // 더미 아이템 리스트에 추가
        dummyItems.add(item);
        
        // 아이템을 저장
        itemRepository.save(item);

        return dummyItems;
    }
}
