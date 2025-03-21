package kr.co.anabada.chat.controller;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 채팅방 생성
    @PostMapping("/create")
    public Chat_Room createChatRoom(@RequestParam Integer itemNo, @RequestParam Integer buyerNo) {
        return chatService.createChatRoom(itemNo, buyerNo);
    }

    // 메시지 저장
    @PostMapping("/message")
    public Chat_Message sendMessage(@RequestParam Integer roomNo, @RequestParam Integer senderNo, @RequestParam String content) {
        return chatService.saveMessage(roomNo, senderNo, content);
    }
}
