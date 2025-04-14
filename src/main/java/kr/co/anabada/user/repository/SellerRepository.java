package kr.co.anabada.user.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.Seller.SellerGrade;
import kr.co.anabada.user.entity.User;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {
	// 판매자의 전체 판매액 합계 구하기
	@Query("SELECT SUM(s.sellerTotalSales) FROM Seller s")
	BigDecimal sumTotalSales();

	Seller findByUser(User user);

	@Query("SELECT sellerNo FROM Seller")
	List<Integer> findAllSellerNos(); // userProfile

	@Modifying
	@Query("UPDATE Seller SET sellerGrade = :sellerGrade WHERE sellerNo = :sellerNo")
	int updateSellerGrade(@Param("sellerNo") Integer sellerNo, @Param("sellerGrade") SellerGrade sellerGrade); // userProfile

	// UserProfileService : updateSingleSellerStatistics
	@Modifying
	@Query("UPDATE Seller s SET "
			+ "s.sellerActiveItemCnt = :activeItemCount, "
			+ "s.sellerCompletedSellItemCnt = :completedSellItemCount,"
			+ "s.sellerAvgRating = :avgRating, "
			+ "s.sellerTotalSales = :totalSales, "
			+ "s.sellerUpdatedDate = CURRENT_TIMESTAMP "
			+ "WHERE s.sellerNo = :sellerNo")
	int updateDailySellerStats(
			@Param("sellerNo") Integer sellerNo,
			@Param("activeItemCount") int activeItemCount,
			@Param("completedSellItemCount") int completedSellItemCount,
			@Param("avgRating") double avgRating,
			@Param("totalSales") BigDecimal totalSales);
}