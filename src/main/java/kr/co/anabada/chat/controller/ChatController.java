package kr.co.anabada.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.chat.repository.ChatMessageRepository;
import kr.co.anabada.chat.service.ChatService;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.chat.repository.ChatRoomRepository;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 채팅방에 접근하는 메소드
    @PostMapping("/chat/chatRoom")
    public ModelAndView chatRoom(@RequestParam Integer roomNo) {
        // 하드코딩된 User 객체 생성
        User loggedInUser = new User();
        loggedInUser.setUserNo(1); // userNo 1로 설정
        loggedInUser.setUserName("User 1"); // 사용자 이름 설정
        
        // ModelAndView 객체 생성
        ModelAndView modelAndView = new ModelAndView("chat/chatRoom");

        // 채팅방 정보 가져오기 (기존 채팅방이 없으면 새로 생성)
        Chat_Room chatRoom = chatService.getChatRoom(roomNo);
        if (chatRoom == null) {
            // 더미 아이템 생성
            List<Item> dummyItems = chatService.createDummyItems();
            Item item = dummyItems.get(0); // 첫 번째 더미 아이템 사용
            User receiverUser = getReceiverUserByRoomNo(roomNo); // 수신자 정보 얻기

            // 채팅방 생성
            chatRoom = chatService.createRoom(item, receiverUser);
            roomNo = chatRoom.getRoomNo(); // 새로 생성된 채팅방 번호 설정
        }

        // 해당 채팅방의 메시지 가져오기
        var messages = chatService.getMessagesByRoomNo(roomNo);

        // 뷰에 필요한 데이터 추가
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("roomNo", roomNo);
        modelAndView.addObject("chatRoom", chatRoom);

        return modelAndView;
    }

    // roomNo로 수신자 정보를 찾는 메소드 (하드코딩된 수신자 반환)
    private User getReceiverUserByRoomNo(Integer roomNo) {
        User receiverUser = new User();
        receiverUser.setUserNo(2); // userNo 2로 설정
        receiverUser.setUserName("User 2"); // 사용자 이름 설정
        return receiverUser;
    }
}
