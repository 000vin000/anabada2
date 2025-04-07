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
    private JwtAuthHelper jwtAuthHelper;  // ✅ JWT 인증 헬퍼 추가

    @GetMapping("/chatRoom")
    public String chatRoom(@RequestParam("roomNo") int roomNo, Model model, HttpServletRequest request) {
        // ✅ JWT에서 userNo 추출
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(request);
        Integer loggedInUserNo = (user != null) ? user.getUserNo() : null;

        // ✅ 메시지 가져오기
        List<ChatMessageDTO> messages = chatMessageService.getMessagesByRoomNo(roomNo);

        // ✅ Model에 데이터 추가
        model.addAttribute("messages", messages);
        model.addAttribute("roomNo", roomNo);
        model.addAttribute("loggedInUserNo", loggedInUserNo);

        // ✅ JSP 페이지 반환
        return "chat/chatRoom";  
    }
}
