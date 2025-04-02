package kr.co.anabada.chat.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.anabada.chat.entity.Chat_Message;

@Controller
@RequestMapping("/chat")
public class ChatController {
    
    @GetMapping
    public String chatPage() {
        return "chat/chatPage"; 
    }
    
}
