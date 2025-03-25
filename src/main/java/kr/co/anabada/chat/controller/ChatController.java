package kr.co.anabada.chat.controller;

import kr.co.anabada.chat.dto.ChatMessageDTO;
import kr.co.anabada.chat.dto.CreateChatRoomRequest;
import kr.co.anabada.chat.dto.SendMessageDTO;
import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.handler.UnauthorizedAccessException;
import kr.co.anabada.chat.repository.ChatRoomRepository;
import kr.co.anabada.chat.service.ChatMessageService;
import kr.co.anabada.chat.service.ChatRoomService;
import kr.co.anabada.jwt.JwtUtil;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private JwtUtil jwtUtil;  // JWT 유틸 추가

    // 현재 로그인된 사용자 ID 가져오기 (JWT 토큰 기반)
    private Integer getCurrentUserId(HttpServletRequest request) {
        // 1. 요청 헤더에서 JWT 토큰 추출
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // "Bearer " 부분을 제거하여 실제 토큰만 추출
        }

        // 2. 토큰 검증 및 userId 추출
        if (token != null && jwtUtil.validateToken(token)) {
            try {
                return Integer.parseInt(jwtUtil.extractUserId(token));
            } catch (NumberFormatException e) {
                log.error("잘못된 토큰 형식입니다.", e);
                throw new UnauthorizedAccessException("잘못된 토큰 형식입니다.");
            }
        }

        log.warn("No token found in Authorization header.");
        throw new UnauthorizedAccessException("사용자 인증 실패: 토큰 없음 또는 유효하지 않음.");
    }


    // 문의하기 페이지 (채팅 목록 확인)
    @GetMapping("/inquire")
    public String inquire(HttpServletRequest request, Model model) {
        Integer currentUserId = getCurrentUserId(request);

        // 로그인된 사용자에 해당하는 채팅방 리스트 조회
        List<Chat_Room> chatRooms = chatRoomService.getChatRoomsByUser(currentUserId);

        if (chatRooms.isEmpty()) {
            model.addAttribute("noChat", true);  // 채팅방이 없을 경우
        } else {
            model.addAttribute("chatRooms", chatRooms);  // 채팅방 리스트 전달
        }

        return "chat/chatRoomList";  // 채팅방 목록 뷰 반환
    }
    
    // 채팅 메시지 전송 (JWT + 세션 사용자 비교)    
    @PostMapping("/sendMessage")
    @ResponseBody
    public ResponseEntity<?> sendMessage(HttpServletRequest request, @RequestBody SendMessageDTO requestDTO) {
        Integer senderId = getCurrentUserId(request);
        if (senderId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "error", "message", "로그인 정보가 유효하지 않습니다."));
        }

        // 채팅방 조회 (판매자와 구매자가 동일한 채팅방 찾기)
        Optional<Chat_Room> chatRoomOptional = chatRoomService.getChatRoomByRoomNo(requestDTO.getRoomNo());
        
        if (!chatRoomOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "message", "채팅방이 존재하지 않습니다."));
        }
        
        Chat_Room chatRoom = chatRoomOptional.get();

        // 발신자 정보 가져오기
        Optional<User> senderOptional = userRepository.findById(senderId);
        if (!senderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "message", "발신자 정보가 유효하지 않습니다."));
        }
        User sender = senderOptional.get();

        // 메시지 생성
        Chat_Message message = new Chat_Message();
        message.setMsgContent(requestDTO.getMsgContent());
        message.setMsgIsRead(false);
        message.setMsgDate(LocalDateTime.now());
        message.setChatRoom(chatRoom);
        message.setSender(sender);

        try {
            // 메시지 저장
            chatMessageService.saveMessage(message);
            return ResponseEntity.ok(Map.of("status", "success", "message", "전송완료."));
        } catch (Exception e) {
            log.error("Failed to send message for chatRoom: {}, senderId: {}", requestDTO.getRoomNo(), senderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "메시지 전송 실패."));
        }
    }

    // 채팅 메세지 조회
    @GetMapping("/getMessages/{roomNo}")
    public String getMessages(@PathVariable Integer roomNo, Model model) {
        List<ChatMessageDTO> chatMessages = chatMessageService.getMessagesByRoomNo(roomNo);

        // 메시지 날짜 포맷 변환
        for (ChatMessageDTO message : chatMessages) {
            String formattedDate = message.getMsgDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            message.setFormattedMsgDate(formattedDate); 
        }

        model.addAttribute("chatMessages", chatMessages);
        model.addAttribute("roomNo", roomNo);

        return "chat/chatRoom"; 
    }

    @PostMapping("/createRoom")
    public ResponseEntity<?> createChatRoom(@RequestBody CreateChatRoomRequest dto, HttpServletRequest request) {
        try {
            Chat_Room chatRoom = chatRoomService.createChatRoom(dto.getSellerId(), dto.getBuyerId(), dto.getItemTitle(), dto.getItemNo());
            return ResponseEntity.ok().body(Map.of("status", "success", "chatRoom", chatRoom));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
   
    // 채팅 목록 조회
    @GetMapping("/chatRoomList")
    public String chatRoomList(HttpServletRequest request, Model model) {
        Integer currentUserId = getCurrentUserId(request);  

        if (currentUserId == null) {
            model.addAttribute("error", "사용자 인증 실패.");
            return "error";
        }

        List<Chat_Room> chatRooms = chatRoomRepository.findBySeller_User_UserNoOrBuyer_UserNo(currentUserId, currentUserId);

        // 채팅방이 없으면 채팅 생성 버튼을 표시
        if (chatRooms.isEmpty()) {
            model.addAttribute("noChat", true);  
        } else {
            model.addAttribute("chatRooms", chatRooms); 
        }

        return "chat/chatRoomList";  
    }

}
