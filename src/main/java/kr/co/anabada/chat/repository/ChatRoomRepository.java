package kr.co.anabada.chat.repository;

import kr.co.anabada.chat.entity.Chat_Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chat_Room, Integer> {

    // 채팅방 번호(roomNo)로 채팅방 찾기
    // roomNo를 기준으로 채팅방을 조회
    Optional<Chat_Room> findByRoomNo(Integer roomNo);

    // 특정 사용자의 모든 채팅방 조회
    // 판매자(userNo) 또는 구매자(userNo)를 기준으로 채팅방을 조회
    List<Chat_Room> findBySeller_User_UserNoOrBuyer_UserNo(Integer sellerUserNo, Integer buyerUserNo);

    // 특정 구매자가 특정 상품에 대해 이미 채팅방이 있는지 확인
    // 구매자(buyerUserNo)와 상품(itemNo)에 대해 채팅방이 존재하는지 확인
    boolean existsByBuyer_UserNoAndItemNo(Integer buyerUserNo, Integer itemNo);

    // 특정 판매자와 특정 구매자가 특정 상품에 대해 채팅한 채팅방 찾기
    // 판매자(sellerUserNo), 구매자(buyerUserNo), 상품(itemNo) 기준으로 채팅방을 조회
    Optional<Chat_Room> findBySeller_User_UserNoAndBuyer_UserNoAndItemNo(Integer sellerUserNo, Integer buyerUserNo, Integer itemNo);

    Optional<Chat_Room> findByBuyer_UserNoAndItemNo(Integer buyerUserNo, Integer itemNo);
}
