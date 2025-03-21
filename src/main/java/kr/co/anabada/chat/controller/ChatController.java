package kr.co.anabada.chat.controller;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.service.ChatMessageService;
import kr.co.anabada.chat.service.ChatRoomService;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private UserRepository userRepository;

    // 채팅방 생성
    @PostMapping("/createRoom")
    public Chat_Room createRoom(@RequestParam Integer sellerId, @RequestParam Integer buyerId, @RequestParam String itemTitle, @RequestParam Integer itemNo) {
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));
        User buyer = userRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("Buyer not found"));
        
        return chatRoomService.createChatRoom(seller, buyer, itemTitle, itemNo);
    }

    // 메시지 전송
    @PostMapping("/sendMessage")
    public Chat_Message sendMessage(@RequestParam Integer roomId, @RequestParam Integer senderId, @RequestParam String messageContent) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        return chatMessageService.sendMessage(roomId, sender, messageContent);
    }

    // 특정 채팅방의 메시지 목록 가져오기
    @GetMapping("/getMessages/{roomId}")
    public List<Chat_Message> getMessages(@PathVariable Integer roomId) {
        return chatMessageService.getMessagesByRoomId(roomId);
    }
}
