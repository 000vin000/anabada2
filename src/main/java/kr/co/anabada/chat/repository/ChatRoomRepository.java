package kr.co.anabada.chat.repository;

import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<Chat_Room, Long> {

    // 상품 번호(itemNo)와 판매자/구매자 정보로 채팅방 찾기
    Optional<Chat_Room> findById(Integer id);
}
