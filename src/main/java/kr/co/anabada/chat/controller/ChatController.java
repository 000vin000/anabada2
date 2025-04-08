package kr.co.anabada.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.service.ChatMessageService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;
    
    @Autowired
    private JwtAuthHelper jwtAuthHelper;  

    @GetMapping("/chatRoom")
    public String chatRoom(@RequestParam("roomNo") int roomNo, Model model, HttpServletRequest request) {
        
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(request);
        Integer loggedInUserNo = (user != null) ? user.getUserNo() : null;
      
        List<ChatMessageDTO> messages = chatMessageService.getMessagesByRoomNo(roomNo);
     
        model.addAttribute("messages", messages);
        model.addAttribute("roomNo", roomNo);
        model.addAttribute("loggedInUserNo", loggedInUserNo);
   
        return "chat/chatRoom";  
    }
}
