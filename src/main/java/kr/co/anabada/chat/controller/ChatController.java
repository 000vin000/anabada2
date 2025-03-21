package kr.co.anabada.chat.controller;

import kr.co.anabada.chat.dto.SendMessageRequest;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.chat.service.ChatMessageService;
import kr.co.anabada.chat.service.ChatRoomService;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus; 

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // 채팅방 생성
    @PostMapping("/createRoom")
    public Chat_Room createChatRoom(@RequestParam Integer sellerId, @RequestParam Integer buyerId, 
                                    @RequestParam String itemTitle, @RequestParam Integer itemNo) {
        return chatRoomService.createChatRoom(sellerId, buyerId, itemTitle, itemNo);
    }
    
    
    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request) {
        // roomId로 Chat_Room을 찾기
        Optional<Chat_Room> chatRoomOptional = chatRoomRepository.findById(request.getRoomId());
        
        if (!chatRoomOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid room ID.");
        }
        
        Chat_Room chatRoom = chatRoomOptional.get();

        // 메시지 생성
        Chat_Message message = new Chat_Message();
        message.setMsgContent(request.getMsgContent());
        message.setMsgIsRead(false);
        message.setMsgDate(LocalDateTime.now());
        message.setChatRoom(chatRoom);  // chatRoom을 설정
        message.setSender(request.getSender());  // sender 설정

        // 메시지 저장
        chatMessageService.saveMessage(message);  // saveMessage 메서드 호출
        
        return ResponseEntity.ok("Message sent successfully.");
    }



    // 특정 채팅방의 메시지 목록 가져오기
    @GetMapping("/getMessages/{roomId}")
    public List<Chat_Message> getMessages(@PathVariable Integer roomId) {
        return chatMessageService.getMessagesByRoomId(roomId);
    }
}
