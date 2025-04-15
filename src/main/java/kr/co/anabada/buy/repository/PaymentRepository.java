package kr.co.anabada.buy.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.buy.entity.Payment;
import kr.co.anabada.buy.entity.Payment.PayStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // 하루 결제 금액 합산 (날짜별)
    @Query("SELECT p.payDate, SUM(p.payPrice) " +
           "FROM Payment p WHERE p.payStatus = 'PAID' AND p.payCompletedDate BETWEEN :startDate AND :endDate " +
           "GROUP BY p.payDate ORDER BY p.payDate")
    List<Object[]> sumTotalSalesByDateRange(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);

    // 일주일 결제 금액 합산
    @Query("SELECT SUM(p.payPrice) FROM Payment p WHERE p.payStatus = 'PAID' AND p.payCompletedDate BETWEEN :startDate AND :endDate")
    Long sumTotalSalesByWeek(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 월매출 데이터 구하기 (월별)
    @Query("SELECT EXTRACT(MONTH FROM p.payCompletedDate) AS month, SUM(p.payPrice) AS totalSales " +
    	       "FROM Payment p " +
    	       "WHERE p.payStatus = 'PAID' AND p.payCompletedDate BETWEEN :startDate AND :endDate " +
    	       "GROUP BY EXTRACT(MONTH FROM p.payCompletedDate) " +
    	       "ORDER BY month")
    	List<Object[]> sumTotalSalesByMonth(@Param("startDate") LocalDateTime startDate, 
    	                                     @Param("endDate") LocalDateTime endDate);

    
    @Query("SELECT p FROM Payment p WHERE p.payCompletedDate BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p.payNo, p.payPrice, p.payCompletedDate " +
    	       "FROM Payment p WHERE p.payStatus = 'PAID' AND p.payCompletedDate BETWEEN :startDate AND :endDate " +
    	       "ORDER BY p.payCompletedDate")
    	List<Object[]> sumTotalSalesByDateRangeWithPaymentId(@Param("startDate") LocalDateTime startDate, 
    	                                                     @Param("endDate") LocalDateTime endDate);
    
	// UserProfileScheduler : totalSales (from updateDailyStatistics)
	@Query("SELECT p.order.item.seller.sellerNo, SUM(p.payPrice) "
			+ "FROM Payment p "
			+ "WHERE p.payStatus = :status "
			+ "GROUP BY p.order.item.seller.sellerNo")
	List<Object[]> sumSalesPerSellerByPayStatus(@Param("status") PayStatus status);

	// UserProfileScheduler : completedSellItemCounts (from updateDailyStatistics)
	@Query("SELECT p.order.item.seller.sellerNo, COUNT(p.payNo) "
			+ "FROM Payment p "
			+ "WHERE p.payStatus = :status "
			+ "GROUP BY p.order.item.seller.sellerNo")
	List<Object[]> countPaysPerSellerByPayStatus(@Param("status") PayStatus status);

	// UserProfileScheduler : completedPayCounts (from updateDailyStatistics)
	@Query("SELECT p.order.buyer.buyerNo, COUNT(p.payNo) "
			+ "FROM Payment p "
			+ "WHERE p.payStatus = :status "
			+ "GROUP BY p.order.buyer.buyerNo")
	List<Object[]> countPaysPerBuyerByPayStatus(@Param("status") PayStatus status);
	
    // UserProfileScheduler : grade (from updateMonthlyStatistics)
	@Query("SELECT p.order.item.seller.sellerNo, COUNT(p.payNo) "
			+ "FROM Payment p "
			+ "WHERE p.payStatus = :status "
			+ "AND p.payCompletedDate >= :startDate "
			+ "AND p.payCompletedDate < :endDate "
			+ "GROUP BY p.order.item.seller.sellerNo")
	List<Object[]> countMonthlyCompletedSalesPerSeller(
			@Param("status") PayStatus status,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);
}
