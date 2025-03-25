package kr.co.anabada.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.anabada.chat.entity.Chat_Message;

@Controller
@RequestMapping("/chat")
public class WebSocketController {

    @MessageMapping("/chat/{roomNo}")
    @SendTo("/topic/chat/{roomNo}")
    public Chat_Message sendMessage(@DestinationVariable String roomNo, Chat_Message message) {
        return message; 
    }
}

