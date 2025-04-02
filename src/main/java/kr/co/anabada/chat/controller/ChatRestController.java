package kr.co.anabada.chat.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.dto.ChatRoomDTO;
import kr.co.anabada.chat.dto.ChatRoomRequestDto;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.chat.service.ChatMessageService;
import kr.co.anabada.chat.service.ChatRoomService;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ItemService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    private static final Logger logger = LoggerFactory.getLogger(ChatRestController.class);

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private JwtAuthHelper jwtAuthHelper;

    @Autowired
    private ItemService itemService;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    
    // 기존 채팅 메시지 조회
    @GetMapping("/messages/{roomNo}")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Integer roomNo) { 
        List<ChatMessageDTO> chatMessages = chatMessageService.getMessagesByRoomNo(roomNo);
        return ResponseEntity.ok(chatMessages);
    }


    // 메시지 전송, 저장 
    @PostMapping("/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(@RequestParam Integer roomNo,  
                                                      @RequestParam String msgContent, 
                                                      @RequestParam Integer senderNo) {  
        ChatMessageDTO chatMessageDTO = chatMessageService.sendMessage(roomNo, msgContent, senderNo);
        return ResponseEntity.ok(chatMessageDTO);
    }
    
    

   
    // 채팅방 정보 조회 API 추가
    @GetMapping("/{roomNo}")
    public ResponseEntity<?> getChatRoom(@PathVariable Integer roomNo) {
        Chat_Room chatRoom = chatRoomService.findChatRoomById(roomNo);
        if (chatRoom == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("채팅방을 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/rooms/{itemNo}")
    public ResponseEntity<List<ChatRoomDTO>> getChatRoomsByItem(@PathVariable Integer itemNo, HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        Integer userNo = user.getUserNo();
        List<Chat_Room> chatRooms = chatRoomService.getChatRoomsByItem(itemNo, userNo);
        List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream().map(chatRoom -> {
            Item item = itemService.findById(chatRoom.getItemNo());
            logger.info("Fetched item: " + (item != null ? item.getItemTitle() : "Item not found"));
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setRoomNo(chatRoom.getRoomNo());
            chatRoomDTO.setItemTitle(item != null ? item.getItemTitle() : "Unknown Title");
            chatRoomDTO.setItemNo(item != null ? item.getItemNo() : null);
            chatRoomDTO.setCreatedAt(chatRoom.getCreatedAt());
            return chatRoomDTO;
        }).toList();

        return ResponseEntity.ok(chatRoomDTOs);
    }

    @PostMapping("/rooms")
    public ResponseEntity<Chat_Room> createChatRoom(@RequestBody Map<String, Object> requestData, HttpServletRequest req) {
        Integer itemNo = (Integer) requestData.get("itemNo");
        String itemTitle = (String) requestData.get("itemTitle");

        if (itemNo == null || itemTitle == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Item item = itemService.findById(itemNo);
        if (item == null) {
            logger.error("❌ Item not found for itemNo: " + itemNo);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (item.getSeller() == null) {
            logger.error("❌ Seller not found for itemNo: " + itemNo);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("✅ Seller ID: " + item.getSeller().getUser().getUserNo());



        
        Integer sellerNo = item.getSeller().getUser().getUserNo();

        UserTokenInfo buyer = jwtAuthHelper.getUserFromRequest(req);
        if (buyer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean roomExists = chatRoomService.existsByBuyerAndItem(buyer.getUserNo(), itemNo);
        if (roomExists) {
            return ResponseEntity.status(HttpStatus.FOUND).body(null);
        }

        Chat_Room chatRoom = chatRoomService.createChatRoom(sellerNo, buyer.getUserNo(), itemTitle, itemNo);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoom);
    }

    @GetMapping("/seller/{itemNo}")
    public ResponseEntity<Map<String, Integer>> getSellerId(@PathVariable Integer itemNo) {
        Item item = itemService.findById(itemNo);
        if (item == null || item.getSeller() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Integer sellerNo = item.getSeller().getSellerNo(); // 판매자의 sellerNo 가져오기
        return ResponseEntity.ok(Map.of("sellerNo", sellerNo));
    }



    @PostMapping("/room")
    public ResponseEntity<?> findChatRoom(@RequestBody ChatRoomRequestDto requestDto) {
        Integer sellerUserNo = requestDto.getSellerUserNo();
        Integer buyerUserNo = requestDto.getBuyerUserNo();
        Integer itemNo = requestDto.getItemNo();

        Optional<Chat_Room> chatRoom = chatRoomRepository.findBySeller_User_UserNoAndBuyer_UserNoAndItemNo(
            sellerUserNo, buyerUserNo, itemNo
        );
        
        if (chatRoom.isPresent()) {
            return ResponseEntity.ok(Collections.singletonMap("roomNo", chatRoom.get().getRoomNo()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 채팅방이 없습니다.");
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Map<String, String>> sendMessage(
            @RequestParam Integer roomNo,
            @RequestParam String message,
            HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }

        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("message", "메시지가 비어 있습니다."));
        }

        boolean roomExists = chatRoomService.existsByRoomNo(roomNo);
        if (!roomExists) {
            return ResponseEntity.status(404).body(Map.of("message", "채팅방을 찾을 수 없습니다."));
        }

        chatMessageService.sendMessage(roomNo, message, user.getUserNo());
        return ResponseEntity.ok(Map.of("message", "메시지 전송 성공"));
    }
    
    @GetMapping("/room")
    public ResponseEntity<Chat_Room> getOrCreateChatRoom(
            @RequestParam Integer sellerId,
            @RequestParam Integer buyerId,
            @RequestParam Integer itemNo,
            @RequestParam String itemTitle) {

        Chat_Room chatRoom = chatRoomService.findOrCreateChatRoom(sellerId, buyerId, itemNo, itemTitle);
        return ResponseEntity.ok(chatRoom);
    }
    
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chatRoom.{roomNo}")
    public Chat_Message sendMessage(@Payload Chat_Message chatMessage) {
        chatMessageService.saveMessage(chatMessage);
        chatMessage.formatMsgDate();
        return chatMessage;
    }


}
