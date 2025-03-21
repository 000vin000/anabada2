package kr.co.anabada.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.user.entity.User;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chat_Room, Integer> {
    
    // 채팅방을 방 번호로 조회
    Optional<Chat_Room> findByRoomNo(Integer roomNo);
    
    // 특정 사용자와 특정 상품에 해당하는 채팅방 조회
    Optional<Chat_Room> findByItemNoAndUser1OrUser2(Integer itemNo, User user1, User user2);

    // 두 사용자가 참여한 채팅방 조회 (방향 무관)
    Optional<Chat_Room> findByUser1AndUser2OrUser2AndUser1(User user1, User user2, User user3, User user4);
}
