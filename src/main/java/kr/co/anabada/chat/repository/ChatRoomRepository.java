package kr.co.anabada.chat.repository;

import kr.co.anabada.chat.entity.Chat_Room;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chat_Room, Integer> {

    // 채팅방 번호(roomNo)로 채팅방 찾기
    Optional<Chat_Room> findByRoomNo(Integer roomNo);

    // 특정 사용자의 모든 채팅방 조회
    List<Chat_Room> findBySeller_User_UserNoOrBuyer_UserNo(Integer sellerUserNo, Integer buyerUserNo);

    // 특정 구매자가 특정 상품에 대해 이미 채팅방이 있는지 확인
    boolean existsByBuyer_UserNoAndItemNo(Integer buyerUserNo, Integer itemNo);
    
    // 채팅방 목록 (판매자)
    List<Chat_Room> findAllByItemNo(Integer itemNo);

    Optional<Chat_Room> findByBuyer_UserNoAndItemNo(Integer buyerUserNo, Integer itemNo);

    // 특정 아이템에 대한 채팅방 조회
    @Query("SELECT cr FROM Chat_Room cr JOIN FETCH cr.seller JOIN FETCH cr.buyer WHERE cr.roomNo = :roomNo")
    Optional<Chat_Room> findByRoomNoWithSellerAndBuyer(@Param("roomNo") Integer roomNo);
    
    // 채팅방 저장 및 조회
    Optional<Chat_Room> findBySellerAndBuyerAndItemNo(Seller seller, User buyer, Integer itemNo);
    
    // 특정 사용자가 참여한 특정 아이템의 채팅방 조회
    List<Chat_Room> findByItemNoAndSeller_User_UserNoOrBuyer_UserNo(Integer itemNo, Integer sellerUserNo, Integer buyerUserNo);
    
    // 기존 채팅방 조회
    Optional<Chat_Room> findBySeller_User_UserNoAndBuyer_UserNoAndItemNo(Integer sellerUserNo, Integer buyerUserNo, Integer itemNo);
    

}
