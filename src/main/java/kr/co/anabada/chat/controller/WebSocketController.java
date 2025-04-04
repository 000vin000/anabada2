package kr.co.anabada.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.service.ChatMessageService;

@Controller
@RequestMapping("/chat")
public class WebSocketController {

    @Autowired
    private ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage") // 클라이언트에서 "/app/chat.sendMessage"로 메시지가 오면 이 메서드 실행
    @SendTo("/topic/room/{roomNo}")      // 해당 roomNo 구독자에게 메시지 전송
    public ChatMessageDTO sendMessageToRoom(ChatMessageDTO chatMessageDTO) {
        // 메시지를 데이터베이스에 저장하는 부분
        chatMessageService.sendMessage(chatMessageDTO.getRoomNo(), chatMessageDTO.getMsgContent(), chatMessageDTO.getSenderNo());
        
        // 메시지를 클라이언트에 전송
        return chatMessageDTO;  // 메시지를 다시 클라이언트에 보내는 부분
    }
}
