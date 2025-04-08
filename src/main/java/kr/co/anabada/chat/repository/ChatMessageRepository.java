package kr.co.anabada.chat.repository;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ChatMessageRepository extends CrudRepository<Chat_Message, Integer> {
	// ChatRoom의 roomNo를 기준으로 메시지 조회
	List<Chat_Message> findByChatRoom_RoomNo(Integer roomNo);
          
	// 채팅 메세지 저장 및 조회   
	List<Chat_Message> findByChatRoom_RoomNoOrderByMsgDateAsc(Integer roomNo);

   
   
   

}