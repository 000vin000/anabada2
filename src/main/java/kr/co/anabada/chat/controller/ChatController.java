package kr.co.anabada.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChatController {

    @GetMapping("/chat/chatRoom")
    public ModelAndView chatRoom(@RequestParam Integer roomNo) {
        ModelAndView chatRoom = new ModelAndView("chat/chatRoom");
        chatRoom.addObject("roomNo", roomNo);
        return chatRoom;
    }
}

