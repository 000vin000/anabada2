package kr.co.anabada.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.repository.ChatMessageRepository;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping("/messages/{roomNo}")
    public List<Chat_Message> getPreviousMessages(@PathVariable Integer roomNo) {
        return chatMessageRepository.findByChatRoomRoomNo(roomNo);
    }
}