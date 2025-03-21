package kr.co.anabada.chat.controller;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<Chat_Room> createChatRoom(@RequestParam Integer itemNo, @RequestParam Integer buyerNo) {
        Chat_Room chatRoom = chatService.createChatRoom(itemNo, buyerNo);
        return new ResponseEntity<>(chatRoom, HttpStatus.CREATED);  
    }

    // 메시지 저장
    @PostMapping("/message")
    public ResponseEntity<Chat_Message> sendMessage(@RequestBody Chat_Message message) {
        Chat_Message savedMessage = chatService.saveMessage(
                message.getChatRoom().getRoomNo(),
                message.getSender().getUserNo(),
                message.getMsgContent()
        );
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);  
    }
}
