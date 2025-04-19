package kr.co.anabada.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.user.entity.Buyer;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
	Optional<Buyer> findByUserUserNo(Integer userNo); // userProfile

	@Query("SELECT buyerNo FROM Buyer")
	List<Integer> findAllBuyerNos(); // userProfile

	@Query("SELECT b.buyerNo FROM Buyer b WHERE b.user.userNo = :userNo")
	Optional<Integer> findBuyerNoByUserNo(Integer userNo); // userProfile

	@Modifying
	@Query("UPDATE Buyer b SET "
			+ "b.buyerBidCnt = :bidCount, "
			+ "b.buyerActiveBidItemCnt = :activeBidItemCount, "
			+ "b.buyerBidItemCnt = :bidItemCount, "
			+ "b.buyerBidSuccessCnt = :bidSuccessCount, "
			+ "b.buyerPaySuccessCnt = :paySuccessCount, "
			+ "b.buyerBidSuccessRate = :bidSuccessRate, "
			+ "b.buyerPaySuccessRate = :paySuccessRate, "
			+ "b.buyerUpdatedDate = CURRENT_TIMESTAMP "
			+ "WHERE b.buyerNo = :buyerNo")
	int updateDailyBuyerStats(
			@Param("buyerNo") Integer buyerNo,
			@Param("bidCount") int bidCount,
			@Param("activeBidItemCount") int activeBidItemCount,
			@Param("bidItemCount") int bidItemCount,
			@Param("bidSuccessCount") int bidSuccessCount,
			@Param("paySuccessCount") int paySuccessCount,
			@Param("bidSuccessRate") double bidSuccessRate,
			@Param("paySuccessRate") double paySuccessRate);
}
