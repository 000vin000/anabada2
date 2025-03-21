package kr.co.anabada.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.service.ChatService;

@Controller
public class ChatController {
	
	@Autowired
    private final ChatMessageRepository chatMessageRepository;

    public ChatController(ChatService chatService, ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/chat/chatRoom")
    public ModelAndView chatRoom(@RequestParam Integer roomNo) {
        ModelAndView chatRoom = new ModelAndView("chat/chatRoom");
                
        var messages = chatMessageRepository.findByChatRoomRoomNo(roomNo);
        chatRoom.addObject("messages", messages);
        chatRoom.addObject("roomNo", roomNo);
        return chatRoom;
    }
}