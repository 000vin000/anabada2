package kr.co.anabada.buy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.buy.entity.Review;
import kr.co.anabada.item.entity.Bid.BidStatus;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	boolean existsByBidItemItemNoAndBidBuyerBuyerNoAndBidBidStatus(
			@Param("itemNo") Integer itemNo,
			@Param("buyerNo") Integer buyerNo,
			@Param("bidStatus") BidStatus bidStatus); //userProfile
	
	// UserProfileScheduler : updateDailyStatistics
	@Query("SELECT r.seller.sellerNo, AVG(r.reviewRating) "
			+ "FROM Review r "
			+ "WHERE r.seller IS NOT NULL "
			+ "GROUP BY r.seller.sellerNo")
	List<Object[]> averageReviewRatingPerSeller();
}
