package kr.co.anabada.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.user.entity.User;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<Chat_Message, Integer> {

    // 특정 채팅방의 모든 메시지 조회 (roomNo 기준)
    List<Chat_Message> findByChatRoomRoomNo(Integer roomNo);

    // 특정 채팅방의 미읽은 메시지 조회 (roomNo 기준)
    List<Chat_Message> findByChatRoomRoomNoAndMsgIsReadFalse(Integer roomNo);

    // 특정 사용자에 의해 보내진 메시지 조회
    List<Chat_Message> findBySender(User sender);
}
