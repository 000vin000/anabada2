package kr.co.anabada.chat.repository;

import kr.co.anabada.chat.entity.Chat_Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chat_Room, Integer> {

    // 상품 번호(itemNo)와 채팅방 번호(roomNo)로 채팅방 찾기
    Optional<Chat_Room> findByRoomNo(Integer roomNo);

    // seller와 buyer의 userNo로 채팅방 찾기 (Seller와 User의 userNo 필드를 참조)
    List<Chat_Room> findBySeller_User_UserNoOrBuyer_UserNo(Integer sellerUserNo, Integer buyerUserNo);

    // seller와 buyer의 userNo, itemNo로 채팅방이 존재하는지 확인
    boolean existsBySeller_User_UserNoAndBuyer_UserNoAndItemNo(Integer sellerUserNo, Integer buyerUserNo, Integer itemNo);
    
    // seller와 buyer의 userNo, itemNo로 채팅방 찾기
    Optional<Chat_Room> findBySeller_User_UserNoAndBuyer_UserNoAndItemNo(Integer sellerUserNo, Integer buyerUserNo, Integer itemNo);

    // seller의 sellerNo 또는 buyer의 userNo로 채팅방 찾기
    List<Chat_Room> findBySeller_SellerNoOrBuyer_UserNo(Integer sellerNo, Integer buyerNo);

    // seller의 sellerNo, buyer의 userNo, itemNo로 채팅방이 존재하는지 확인
    boolean existsBySeller_SellerNoAndBuyer_UserNoAndItemNo(Integer sellerNo, Integer buyerNo, Integer itemNo);
    
}



