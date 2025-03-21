package kr.co.anabada.chat.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.user.entity.User;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<Chat_Message, Integer> {

    // 채팅방의 id로 읽지 않은 메시지 찾기
    List<Chat_Message> findByChatRoomIdAndMsgIsReadFalse(Integer roomNo);
}