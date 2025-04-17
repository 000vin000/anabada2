package kr.co.anabada.chat.service;

import kr.co.anabada.chat.dto.ChatRoomDTO;
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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository,
                           ChatMessageRepository chatMessageRepository,
                           UserRepository userRepository,
                           SellerRepository sellerRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.sellerRepository = sellerRepository;
    }
    
    // 채팅방 목록 (판매자)
    public List<ChatRoomDTO> findChatRoomsByItemNo(Integer itemNo) {
        // itemNo로 채팅방 목록 조회
        List<Chat_Room> chatRooms = chatRoomRepository.findAllByItemNo(itemNo);

        // Chat_Room 엔티티를 ChatRoomDTO로 변환하여 반환
        return chatRooms.stream()
                        .map(ChatRoomDTO::fromEntity)
                        .collect(Collectors.toList());
    }

    // 특정 구매자가 특정 상품에 대해 이미 채팅방이 있는지 확인
    public boolean existsByRoomNo(Integer roomNo) {
        return chatRoomRepository.findByRoomNo(roomNo).isPresent();  
    }
    
    // 채팅방 생성 시 기존에 있는지 조회 
    public Optional<Chat_Room> getChatRoomByBuyerAndItem(Integer buyerUserNo, Integer itemNo) {
        return chatRoomRepository.findByBuyer_UserNoAndItemNo(buyerUserNo, itemNo);
    }

    // 채팅방 정보 조회 API 추가
    public Chat_Room findChatRoomById(Integer roomNo) {
        Chat_Room chatRoom = chatRoomRepository.findById(roomNo).orElse(null);
        if (chatRoom == null) {
            System.out.println("채팅방을 찾을 수 없습니다. roomNo: " + roomNo);
        } else {
            System.out.println("채팅방 조회 성공: " + chatRoom);
        }
        return chatRoom;
    }
    
    // 기존 채팅방 조회
    public Optional<Chat_Room> findExistingRoom(Integer sellerUserNo, Integer buyerUserNo, Integer itemNo) {
        return chatRoomRepository.findBySeller_User_UserNoAndBuyer_UserNoAndItemNo(sellerUserNo, buyerUserNo, itemNo);
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
    
    // 채팅방 목록에서 마지막 메세지 조회
    public List<ChatRoomDTO> findChatRoomsByItemNoAndUser(Integer itemNo, Integer userNo) {
        List<Chat_Room> chatRooms = chatRoomRepository.findAllByItemNo(itemNo);

        // 채팅방별 마지막 메시지 시간 기준 내림차순 정렬
        return chatRooms.stream()
            .map(room -> {
                Optional<Chat_Message> lastMsgOpt = chatMessageRepository.findTopByChatRoomOrderByMsgDateDesc(room);

                String lastMessage = "";
                LocalDateTime lastMessageTime = null;

                if (lastMsgOpt.isPresent()) {
                    Chat_Message lastMsg = lastMsgOpt.get();
                    lastMessage = lastMsg.getMsgContent();
                    lastMessageTime = lastMsg.getMsgDate();
                }

                int unreadCount = chatMessageRepository.countUnreadMessages(room.getRoomNo(), userNo);

                return ChatRoomDTO.fromEntity(room, lastMessage, lastMessageTime, unreadCount);
            })
            // lastMessageTime이 null인 경우 가장 오래된 날짜로 처리하여 뒤로 밀기
            .sorted((dto1, dto2) -> {
                LocalDateTime t1 = dto1.getLastMessageTime() != null ? dto1.getLastMessageTime() : LocalDateTime.MIN;
                LocalDateTime t2 = dto2.getLastMessageTime() != null ? dto2.getLastMessageTime() : LocalDateTime.MIN;
                return t2.compareTo(t1);  // 내림차순 정렬 (최신이 위로)
            })
            .collect(Collectors.toList());
    }





}
