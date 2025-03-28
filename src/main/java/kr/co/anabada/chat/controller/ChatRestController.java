package kr.co.anabada.chat.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.dto.ChatRoomDTO;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.service.ChatMessageService;
import kr.co.anabada.chat.service.ChatRoomService;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ItemService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    
    
    @GetMapping("/item/{itemNo}")
    public String findById(@PathVariable Integer itemNo, Model model) {
    	 Item item = itemService.findById(itemNo);
         
    	 model.addAttribute("itemNo", itemNo);
    	 model.addAttribute("itemTitle", item.getItemTitle());
      
         return "itemDetail";  
     }

    
    // 특정 사용자의 채팅방 목록 조회
    @GetMapping("/room/list/{userNo}")
    public ResponseEntity<?> getChatRooms(@PathVariable Integer userNo, HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }       
  
        if (!user.getUserNo().equals(userNo)) {
            return ResponseEntity.status(403).body(Map.of("message", "접근 권한이 없습니다."));
        }

        List<Chat_Room> chatRooms = chatRoomService.getChatRoomsByUser(userNo);

        return ResponseEntity.ok(chatRooms);
    }


    // 현재 로그인한 사용자의 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getChatRooms(HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<Chat_Room> chatRooms = chatRoomService.getChatRoomsByUser(user.getUserNo());

        // 각 채팅방에 대해 아이템 정보를 포함한 ChatRoomDTO로 변환
        List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream().map(chatRoom -> {
            Item item = itemService.findById(chatRoom.getItemNo());
            logger.info("Fetched item: " + (item != null ? item.getItemTitle() : "Item not found"));
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setRoomNo(chatRoom.getRoomNo());
            chatRoomDTO.setItemTitle(item != null ? item.getItemTitle() : "Unknown Title");
            chatRoomDTO.setItemNo(item != null ? item.getItemNo() : null);
            return chatRoomDTO;
        }).toList();

        return ResponseEntity.ok(chatRoomDTOs);  // ChatRoomDTO 리스트 반환
    }



    // 특정 상품에 대해 사용자가 채팅한 내역이 있는지 확인
    @GetMapping("/rooms/check/{itemNo}")
    public ResponseEntity<Map<String, Boolean>> checkChatRoom(@PathVariable Integer itemNo, HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", true));
        }

        boolean exists = chatRoomService.existsByBuyerAndItem(user.getUserNo(), itemNo);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // 특정 채팅방의 메시지 목록 조회
    @GetMapping("/rooms/{roomNo}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Integer roomNo) {
        List<ChatMessageDTO> messages = chatMessageService.getMessagesByRoomNo(roomNo);
        if (messages == null) {
            return ResponseEntity.status(404).body(List.of());
        }
        return ResponseEntity.ok(messages);
    }

    // 메시지 전송
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

    // 채팅방 생성 (기존에 존재하면 생성하지 않음)
    @PostMapping("/rooms")
    public ResponseEntity<Chat_Room> createChatRoom(
            @RequestParam Integer sellerId,
            @RequestParam Integer itemNo,
            @RequestParam String itemTitle,
            HttpServletRequest req) {
        UserTokenInfo buyer = jwtAuthHelper.getUserFromRequest(req);
        if (buyer == null) {
            return ResponseEntity.status(401).build();
        }

        Chat_Room chatRoom = chatRoomService.createChatRoom(sellerId, buyer.getUserNo(), itemTitle, itemNo);
        return ResponseEntity.ok(chatRoom);  // Fix: return Chat_Room
    }
}
