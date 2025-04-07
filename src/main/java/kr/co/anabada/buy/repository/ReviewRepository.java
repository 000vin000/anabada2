package kr.co.anabada.buy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.buy.entity.Review;
import kr.co.anabada.item.entity.Bid.BidStatus;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	boolean existsByBidItemItemNoAndBidUserUserNoAndBidBidStatus(
			@Param("itemNo") Integer itemNo,
			@Param("userNo") Integer userNo,
			@Param("bidStatus") BidStatus bidStatus); //userProfile
}
