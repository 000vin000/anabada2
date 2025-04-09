package kr.co.anabada.item.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.entity.Item;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

	int countByItemItemNo(Integer itemNo); // userProfile

	List<Bid> findByItem(Item itemNo); // bidList
	
//    Optional<BigDecimal> findTopBidPriceByUserUserNoAndItemItemNoOrderByBidTimeDesc(
//            @Param("userNo") Integer userNo, 
//            @Param("itemNo") Integer itemNo); // itemDetail

	Optional<Bid> findTopByItemItemNoOrderByBidTimeDesc(Integer itemNo); // itemDetail
}
