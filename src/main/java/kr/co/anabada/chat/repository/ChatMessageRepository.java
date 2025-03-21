package kr.co.anabada.chat.repository;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ChatMessageRepository extends CrudRepository<Chat_Message, Integer> {

    // Chat_Room의 ID 타입이 Integer로 수정되었으므로, 아래 메소드도 Integer로 수정
    List<Chat_Message> findByChatRoomNo(Integer roomNo);

}
