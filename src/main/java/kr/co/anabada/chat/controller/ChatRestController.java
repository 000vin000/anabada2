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

    @Autowired
    private static Logger logger = LoggerFactory.getLogger(ChatRestController.class);

    @Autowired
    private ChatRoomService chatRoomService;
    
    @Autowired
    private ChatMessageService chatMessageService;
    
    @Autowired
    private JwtAuthHelper jwtAuthHelper;
    
    @Autowired
    private ItemService itemService;

    // ✅ 기존 채팅 메시지 조회 API
    @GetMapping("/messages/{roomNo}")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Integer roomNo) { 
        List<ChatMessageDTO> chatMessages = chatMessageService.getMessagesByRoomNo(roomNo);
        return ResponseEntity.ok(chatMessages);
    }

    // ✅ 메시지 전송 및 저장 (POST /api/chat/messages)
    @PostMapping("/messages")
    public ResponseEntity<Map<String, String>> sendMessage(
            @RequestParam Integer roomNo,
            @RequestParam String message,
            HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "메시지가 비어 있습니다."));
        }
        boolean roomExists = chatRoomService.existsByRoomNo(roomNo);
        if (!roomExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "채팅방을 찾을 수 없습니다."));
        }

        chatMessageService.sendMessage(roomNo, message, user.getUserNo());
        return ResponseEntity.ok(Map.of("message", "메시지 전송 성공"));
    }

    // ✅ 채팅방 정보 조회 API
    @GetMapping("/{roomNo}")
    public ResponseEntity<?> getChatRoom(@PathVariable Integer roomNo) {
        Chat_Room chatRoom = chatRoomService.findChatRoomById(roomNo);
        return chatRoom != null ? ResponseEntity.ok(chatRoom) : 
               ResponseEntity.status(HttpStatus.NOT_FOUND).body("채팅방을 찾을 수 없습니다.");
    }
    
    // ✅ 기존 채팅방 조회 API
    @PostMapping("/room")
    public ResponseEntity<?> findExistingChatRoom(@RequestBody ChatRoomRequestDto request) {
        // itemNo를 이용해 item 객체를 가져온 후 sellerNo를 추출
        Item item = itemService.findById(request.getItemNo());
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        }

        Integer sellerNo = item.getSeller().getSellerNo();
        logger.info("✅ 채팅방 조회 요청 - sellerUserNo: {}, buyerUserNo: {}, itemNo: {}", sellerNo, request.getBuyerUserNo(), request.getItemNo());

        // 기존 채팅방 조회
        Optional<Chat_Room> existingRoom = chatRoomService.findExistingRoom(sellerNo, request.getBuyerUserNo(), request.getItemNo());

        if (existingRoom.isPresent()) {
            // 채팅방이 이미 존재하면 기존 채팅방을 반환
            return ResponseEntity.ok(Collections.singletonMap("roomNo", existingRoom.get().getRoomNo()));
        } else {
            // 채팅방이 없으면 새 채팅방을 생성
            return createNewChatRoom(sellerNo, request.getBuyerUserNo(), request.getItemNo());
        }
    }

    private ResponseEntity<?> createNewChatRoom(Integer sellerNo, Integer buyerNo, Integer itemNo) {
        // 채팅방 생성 로직
        Item item = itemService.findById(itemNo);
        String itemTitle = item.getItemTitle(); // 예시로 상품 제목을 채팅방에 포함시킴

        Chat_Room chatRoom = chatRoomService.createChatRoom(sellerNo, buyerNo, itemTitle, itemNo);
        return ResponseEntity.ok(Collections.singletonMap("roomNo", chatRoom.getRoomNo()));
    }


    // ✅ 채팅방 생성 API
    @PostMapping("/rooms")
    public ResponseEntity<?> createChatRoom(@RequestBody Map<String, Object> requestData) {
        try {
            Integer itemNo = Integer.parseInt(requestData.get("itemNo").toString());

            // ✅ itemNo로 Item 객체를 가져와 sellerNo를 추출
            Item item = itemService.findById(itemNo);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
            }

            Integer sellerNo = item.getSeller().getSellerNo();
            Integer buyerNo = Integer.parseInt(requestData.get("buyerNo").toString());
            String itemTitle = requestData.get("itemTitle").toString();

            logger.info("✅ 채팅방 생성 요청 - sellerNo: {}, buyerNo: {}, itemNo: {}", sellerNo, buyerNo, itemNo);

            // 채팅방 생성
            Chat_Room chatRoom = chatRoomService.createChatRoom(sellerNo, buyerNo, itemTitle, itemNo);
            return ResponseEntity.ok(chatRoom);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("잘못된 숫자 형식입니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
}
