package kr.co.anabada.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.chat.entity.Chat_Room;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chat_Room, Integer> {
	
    // 채팅방 목록 (판매자)
    List<Chat_Room> findAllByItemNo(Integer itemNo);

    // 채팅방 번호(roomNo)로 채팅방 찾기
    Optional<Chat_Room> findByRoomNo(Integer roomNo);
    
    // 채팅방 생성 시 기존에 있는지 조회 
    Optional<Chat_Room> findByBuyer_UserNoAndItemNo(Integer buyerUserNo, Integer itemNo);
    
    // 기존 채팅방 조회
    Optional<Chat_Room> findBySeller_User_UserNoAndBuyer_UserNoAndItemNo(Integer sellerUserNo, Integer buyerUserNo, Integer itemNo);
    
    

        

    

}
