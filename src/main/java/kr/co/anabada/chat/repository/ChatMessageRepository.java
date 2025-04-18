package kr.co.anabada.chat.repository;

import kr.co.anabada.chat.entity.Chat_Message;
import kr.co.anabada.chat.entity.Chat_Room;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends CrudRepository<Chat_Message, Integer> {
	// ChatRoom의 roomNo를 기준으로 메시지 조회
	List<Chat_Message> findByChatRoom_RoomNo(Integer roomNo);
          
	// 채팅 메세지 저장 및 조회   
	List<Chat_Message> findByChatRoom_RoomNoOrderByMsgDateAsc(Integer roomNo);

	// 사용자의 메시지 조회 (추가된 부분)
    List<Chat_Message> findBySender_UserNo(Integer userNo);
   
    // 마지막 메세지 조회
    Optional<Chat_Message> findTopByChatRoomOrderByMsgDateDesc(Chat_Room chatRoom);
    
    // 읽지 않은 메시지 개수 조회 (채팅방 + 로그인 유저 기준)
    @Query("SELECT COUNT(m) FROM Chat_Message m WHERE m.chatRoom.roomNo = :roomNo AND m.msgIsRead = false AND m.sender.userNo <> :userNo")
    int countUnreadMessages(@Param("roomNo") Integer roomNo, @Param("userNo") Integer userNo);


   

}