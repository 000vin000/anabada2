package kr.co.anabada.item.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.entity.Bid.BidStatus;
import kr.co.anabada.item.entity.Item;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

	int countByItemItemNo(Integer itemNo); // userProfile

	List<Bid> findByItem(Item itemNo); // bidList

	Optional<Bid> findTopByItemItemNoOrderByBidTimeDesc(Integer itemNo); // itemDetail

	// UserProfileScheduler : bidCounts (from updateDailyStatistics)
	@Query("SELECT b.buyer.buyerNo, COUNT(b.bidNo) "
			+ "FROM Bid b "
			+ "GROUP BY b.buyer.buyerNo")
	List<Object[]> countAllBidsPerBuyer();

	// UserProfileScheduler : bidSuccessCounts (from updateDailyStatistics)
	@Query("SELECT b.buyer.buyerNo, COUNT(b.bidNo) "
			+ "FROM Bid b "
			+ "WHERE b.bidStatus = :status "
			+ "GROUP BY b.buyer.buyerNo")
	List<Object[]> countBidsPerBuyerByBidStatus(@Param("status") BidStatus status);
}
