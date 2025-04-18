package kr.co.anabada.item.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.entity.Bid.BidStatus;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.Buyer;

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
	
	// ItemStatusScheduler
	@Query("SELECT b.buyer "
			+ "FROM Bid b "
			+ "WHERE b.item = :item "
			+ "ORDER BY b.bidPrice DESC, b.bidTime ASC")
    Optional<Buyer> findWinningBuyerByItem(@Param("item") Item item);
}
