package kr.co.anabada.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.User;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chat_Room, Integer> {
    
    // 채팅방을 방 번호로 조회
    Optional<Chat_Room> findByRoomNo(Integer roomNo);
    
    // 특정 사용자와 특정 상품에 해당하는 채팅방 조회
    Optional<Chat_Room> findByItemAndSenderNoOrReceiverNo(Item item, User senderNo, User receiverNo);
    
   
    
    // 기존 채팅방이 존재하는지 확인하는 메서드 추가
    Chat_Room findByItemAndSenderNoAndReceiverNo(Item item, User senderNo, User receiverNo);
}
