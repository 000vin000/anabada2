package kr.co.anabada.chat.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.dto.ChatRoomDTO;
import kr.co.anabada.chat.dto.ChatRoomRequestDto;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.service.ChatMessageService;
import kr.co.anabada.chat.service.ChatRoomService;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ItemService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.service.UserService;

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
    private UserService userService;

    // 기존 채팅 메시지 조회 
    @GetMapping("/messages/{roomNo}")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Integer roomNo) { 
        List<ChatMessageDTO> chatMessages = chatMessageService.getMessagesByRoomNo(roomNo);
        return ResponseEntity.ok(chatMessages);
    }

    // 메시지 전송 및 저장 
    @PostMapping("/messages")
    public ResponseEntity<Map<String, Object>> sendMessage(
            @RequestBody ChatMessageDTO chatMessageDTO, // ChatMessageDTO 객체 사용
            HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }

        Integer roomNo = chatMessageDTO.getRoomNo();
        String message = chatMessageDTO.getMsgContent();

        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "메시지가 비어 있습니다."));
        }
        boolean roomExists = chatRoomService.existsByRoomNo(roomNo);
        if (!roomExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "채팅방을 찾을 수 없습니다."));
        }

        // Chat_Message 객체 생성
        Chat_Message chatMessage = Chat_Message.builder()
                .msgContent(message)
                .msgIsRead(false) // 초기 읽음 상태
                .msgDate(LocalDateTime.now()) // 현재 시간으로 설정
                .chatRoom(chatRoomService.findChatRoomById(roomNo)) // 채팅방 설정
                .sender(userService.getUser(user.getUserNo())) // 발신자 설정
                .build();

        // 메시지를 저장
        Chat_Message savedMessage = chatMessageService.saveMessage(chatMessage);

        // 메시지 읽음 상태 업데이트 (전송 즉시 읽음 처리)
        chatMessageService.updateMessageReadStatus(savedMessage.getMsgNo(), true);

        return ResponseEntity.ok(Map.of("message", "메시지 전송 성공", "msgNo", savedMessage.getMsgNo()));
    }






    // 채팅방 정보 조회 
    @GetMapping("/{roomNo}")
    public ResponseEntity<?> getChatRoom(@PathVariable Integer roomNo) {
        Chat_Room chatRoom = chatRoomService.findChatRoomById(roomNo);
        return chatRoom != null ? ResponseEntity.ok(chatRoom) : 
               ResponseEntity.status(HttpStatus.NOT_FOUND).body("채팅방을 찾을 수 없습니다.");
    }
    
    // 기존 채팅방 조회 
    @PostMapping("/room")
    public ResponseEntity<?> findExistingChatRoom(@RequestBody ChatRoomRequestDto request) {
        // itemNo를 이용해 item 객체를 가져온 후 sellerNo를 추출
        Item item = itemService.findById(request.getItemNo());
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        }

        Integer sellerNo = item.getSeller().getSellerNo();
        logger.info("채팅방 조회 요청 - sellerUserNo: {}, buyerUserNo: {}, itemNo: {}", sellerNo, request.getBuyerUserNo(), request.getItemNo());

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
        // 채팅방 생성
        Item item = itemService.findById(itemNo);
        String itemTitle = item.getItemTitle(); // 상품 제목

        Chat_Room chatRoom = chatRoomService.createChatRoom(sellerNo, buyerNo, itemTitle, itemNo);
        return ResponseEntity.ok(Collections.singletonMap("roomNo", chatRoom.getRoomNo()));
    }


    // 채팅방 생성 
    @PostMapping("/rooms")
    public ResponseEntity<?> createChatRoom(@RequestBody Map<String, Object> requestData) {
        try {
            Integer itemNo = Integer.parseInt(requestData.get("itemNo").toString());

            // itemNo로 Item 객체를 가져와 sellerNo를 추출
            Item item = itemService.findById(itemNo);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
            }

            Integer sellerNo = item.getSeller().getSellerNo();
            Integer buyerNo = Integer.parseInt(requestData.get("buyerNo").toString());
            String itemTitle = requestData.get("itemTitle").toString();

            logger.info("채팅방 생성 요청 - sellerNo: {}, buyerNo: {}, itemNo: {}", sellerNo, buyerNo, itemNo);

            // 채팅방 생성
            Chat_Room chatRoom = chatRoomService.createChatRoom(sellerNo, buyerNo, itemTitle, itemNo);
            return ResponseEntity.ok(chatRoom);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("잘못된 숫자 형식입니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
    
    // 특정 아이템에 대한 전체 채팅방 조회 (판매자용)
    @GetMapping("/rooms/item/{itemNo}")
    public ResponseEntity<?> getChatRoomsForItem(@PathVariable Integer itemNo, HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 아이템 조회
        Item item = itemService.findById(itemNo);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        }

        // 판매자 userNo와 로그인 유저 비교
        Integer sellerUserNo = item.getSeller().getUser().getUserNo(); // ✔️ 핵심!

        if (!user.getUserNo().equals(sellerUserNo)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        List<ChatRoomDTO> chatRooms = chatRoomService.findChatRoomsByItemNo(itemNo);
        return ResponseEntity.ok(chatRooms);
    }
    
    // 메시지 읽음 처리
    @PostMapping("/messages/read/{messageId}")
    public ResponseEntity<Map<String, String>> markMessageAsRead(@PathVariable Integer messageId, HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }

        // 메시지 읽음 상태 업데이트
        chatMessageService.updateMessageReadStatus(messageId, true);
        return ResponseEntity.ok(Map.of("message", "메시지가 읽음으로 처리되었습니다."));
    }


}
