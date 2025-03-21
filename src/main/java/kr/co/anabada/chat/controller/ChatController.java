package kr.co.anabada.chat.controller;

import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.service.ChatRoomService;
import kr.co.anabada.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;

    // 판매자와 구매자의 userNo로 채팅방 조회
    @GetMapping("/rooms/{sellerUserNo}/{buyerUserNo}")
    public Optional<Chat_Room> getChatRoomByUsers(@PathVariable Integer sellerUserNo, @PathVariable Integer buyerUserNo) {
        return chatRoomService.getChatRoomByUsers(sellerUserNo, buyerUserNo);
    }

    // 채팅방 생성 (테스트용)
    @PostMapping("/create")
    public Chat_Room createChatRoom(@RequestBody ChatRoomRequest request) {
        User seller = new User();  // 테스트용 판매자 객체
        seller.setUserNo(request.getSellerId());
        
        User buyer = new User();  // 테스트용 구매자 객체
        buyer.setUserNo(request.getBuyerId());

        return chatRoomService.createChatRoom(seller, buyer, request.getItemNo(), request.getItemTitle());
    }

    @Getter
    @Setter
    // 채팅방 생성 요청 데이터 클래스
    public static class ChatRoomRequest {
        private Integer sellerId;
        private Integer buyerId;
        private Integer itemNo;
        private String itemTitle;
    }
}
