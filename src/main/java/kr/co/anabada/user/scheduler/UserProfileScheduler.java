package kr.co.anabada.user.scheduler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.anabada.buy.entity.Payment.PayStatus;
import kr.co.anabada.buy.repository.PaymentRepository;
import kr.co.anabada.buy.repository.ReviewRepository;
import kr.co.anabada.item.entity.Bid.BidStatus;
import kr.co.anabada.item.entity.Item.ItemStatus;
import kr.co.anabada.item.repository.BidRepository;
import kr.co.anabada.item.repository.ItemDetailRepository;
import kr.co.anabada.user.entity.Seller.SellerGrade;
import kr.co.anabada.user.repository.SellerRepository;
import kr.co.anabada.user.service.UserProfileSchedulerService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserProfileScheduler {
	@Autowired
	private ItemDetailRepository itemDetailRepository;
	@Autowired
	private UserProfileSchedulerService userProfileSchedulerService;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private BidRepository bidRepository;
	@Autowired
	private SellerRepository sellerRepository;

	@Scheduled(cron = "0 0 2 * * *")
	public void updateProfileDashboardDaily() {
		log.info("프로필 대시보드 1일 주기 스케줄러 시작");

		// ---Seller
		Map<Integer, Integer> itemCounts = listToMap(
				itemDetailRepository.countItemsPerSellerByOptionalItemStatus(null));
		Map<Integer, Integer> activeItemCounts = listToMap(
				itemDetailRepository.countItemsPerSellerByOptionalItemStatus(ItemStatus.ACTIVE));
		Map<Integer, Integer> completedSellItemCounts = listToMap(
				paymentRepository.countPaysPerSellerByPayStatus(PayStatus.PAID));
		Map<Integer, BigDecimal> totalSales = listToBigDecimalMap(
				paymentRepository.sumSalesPerSellerByPayStatus(PayStatus.PAID));
		Map<Integer, Double> avgRatings = listToAvgMap(
				reviewRepository.averageReviewRatingPerSeller());
		
		log.info("판매한 상품: " + itemCounts.toString());
		log.info("판매중 상품: " + activeItemCounts.toString());
		log.info("판매완료 상품: " + completedSellItemCounts.toString());
		log.info("총 판매액: " + totalSales.toString());
		log.info("판매 평점: " + avgRatings.toString());

		Set<Integer> allSellerNos = new HashSet<>();
		allSellerNos.addAll(itemCounts.keySet());
		allSellerNos.addAll(activeItemCounts.keySet());
		allSellerNos.addAll(completedSellItemCounts.keySet());
		allSellerNos.addAll(totalSales.keySet());
		allSellerNos.addAll(avgRatings.keySet());

		log.info("{}명의 판매자가 업데이트 됩니다.", allSellerNos.size());

		int successSellerCount = 0;
		int failSellerCount = 0;
		for (Integer sellerNo : allSellerNos) {
			try {
				int itemCount = itemCounts.getOrDefault(sellerNo, 0);
		        int activeItemCount = activeItemCounts.getOrDefault(sellerNo, 0);
		        int completedSellItemCount = completedSellItemCounts.getOrDefault(sellerNo, 0);
		        BigDecimal totalSale = totalSales.getOrDefault(sellerNo, BigDecimal.ZERO);
		        double avgRating = avgRatings.getOrDefault(sellerNo, 0.0);

		        double salesSuccessRate = calculateRate(completedSellItemCount, itemCount);
				
				userProfileSchedulerService.updateSingleSellerStatistics(
						sellerNo,
						itemCount,
						activeItemCount,
						completedSellItemCount,
						totalSale,
						avgRating,
						salesSuccessRate);
				successSellerCount++;
			} catch (Exception e) {
				failSellerCount++;
				log.error("판매자 업데이트 실패: {}", sellerNo, e.getMessage(), e);
			}
			log.info("프로필 대시보드 1일 주기 스케줄러(판매자) 완료. 성공: {}, 실패: {}", successSellerCount, failSellerCount);
		}

		// ---Buyer
		Map<Integer, Integer> bidCounts = listToMap(
				bidRepository.countAllBidsPerBuyer());
		Map<Integer, Integer> activeBidItemCounts = listToMap(
				itemDetailRepository.countItemsPerBuyerByItemStatusAndOptionalBidStatus(ItemStatus.ACTIVE, BidStatus.ACTIVE));
		Map<Integer, Integer> bidItemCounts = listToMap(
				itemDetailRepository.countBidItemsPerBuyer());
		Map<Integer, Integer> bidSuccessCounts = listToMap(
				bidRepository.countBidsPerBuyerByBidStatus(BidStatus.WINNING));
		Map<Integer, Integer> paySuccessCounts = listToMap(
				paymentRepository.countPaysPerBuyerByPayStatus(PayStatus.PAID));
		
		log.info("총 입찰 횟수: " + bidCounts.toString());
		log.info("입찰중 상품: " + activeBidItemCounts.toString());
		log.info("입찰한 상품: " + bidItemCounts.toString());
		log.info("낙찰 횟수: " + bidSuccessCounts.toString());
		log.info("결제완료 횟수: " + paySuccessCounts.toString());

		Set<Integer> allBuyerNos = new HashSet<>();
		allBuyerNos.addAll(bidCounts.keySet());
		allBuyerNos.addAll(activeBidItemCounts.keySet());
		allBuyerNos.addAll(bidItemCounts.keySet());
		allBuyerNos.addAll(bidSuccessCounts.keySet());
		allBuyerNos.addAll(paySuccessCounts.keySet());

		int successBuyerCount = 0;
		int failBuyerCount = 0;
		for (Integer buyerNo : allBuyerNos) {
		    try {
		        int bidCount = bidCounts.getOrDefault(buyerNo, 0);
		        int activeBidItemCount = activeBidItemCounts.getOrDefault(buyerNo, 0);
		        int bidItemCount = bidItemCounts.getOrDefault(buyerNo, 0);
		        int bidSuccessCount = bidSuccessCounts.getOrDefault(buyerNo, 0);
		        int paySuccessCount = paySuccessCounts.getOrDefault(buyerNo, 0);

		        double bidSuccessRate = calculateRate(bidSuccessCount, bidItemCount);
		        double paySuccessRate = calculateRate(paySuccessCount, bidSuccessCount);

		        userProfileSchedulerService.updateSingleBuyerStatistics(
		                buyerNo,
		                bidCount,
		                activeBidItemCount,
		                bidItemCount,
		                bidSuccessCount,
		                paySuccessCount,
		                bidSuccessRate,
		                paySuccessRate
		        );
		        successBuyerCount++;
		    } catch (Exception e) {
		        failBuyerCount++;
		        log.error("Failed to update statistics for buyerNo {}: {}", buyerNo, e.getMessage(), e);
		    }
		}
		log.info("프로필 대시보드 1일 주기 스케줄러(구매자) 완료. 성공: {}, 실패: {}", successBuyerCount, failBuyerCount);
	}
	
	@Scheduled(cron = "0 0 3 1 * *")
	public void updateProfileDashboardMonthly() {
		log.info("Starting monthly seller grade update job...");

		YearMonth lastMonth = YearMonth.now().minusMonths(1);
		LocalDateTime startDate = lastMonth.atDay(1).atStartOfDay(); // 지난달 1일 0시 0분 0초
		LocalDateTime endDate = YearMonth.now().atDay(1).atStartOfDay(); // 이번달 1일 0시 0분 0초

		Map<Integer, Integer> monthlySalesCounts;
		try {
			monthlySalesCounts = listToMap(
					paymentRepository.countMonthlyCompletedSalesPerSeller(PayStatus.PAID, startDate, endDate));
			log.info("Calculated last month's sales counts for {} sellers", monthlySalesCounts.size());
		} catch (Exception e) {
			log.error("Failed to calculate monthly sales counts. Aborting grade update", e);
			return;
		}

		List<Integer> allSellerNos;
		try {
			allSellerNos = sellerRepository.findAllSellerNos();
		} catch (Exception e) {
			log.error("Failed to retrieve all seller IDs. Aborting grade update", e);
			return;
		}

		log.info("Processing grades for {} total sellers", allSellerNos.size());
		int successCount = 0;
		int failCount = 0;

		for (Integer sellerNo : allSellerNos) {
			try {
				int itemCount = monthlySalesCounts.getOrDefault(sellerNo, 0);
				SellerGrade newGrade = SellerGrade.fromSalesCount(itemCount);
				userProfileSchedulerService.updateSingleSellerGrade(sellerNo, newGrade);
				successCount++;

			} catch (Exception e) {
				failCount++;
				log.error("Failed to update grade for sellerNo {}: {}", sellerNo, e.getMessage(), e);
			}
		}

		log.info("Monthly seller grade update job finished. Success: {}, Fail: {}", successCount, failCount);
	}

	private double calculateRate(int numerator, int denominator) {
		if (denominator == 0) {
			return 0.0;
		}
		return ((double) numerator / denominator) * 100.0;
	}

	private Map<Integer, Integer> listToMap(List<Object[]> results) {
		return results.stream().collect(Collectors.toMap(
				row -> ((Number) row[0]).intValue(), // ID
				row -> ((Number) row[1]).intValue() // Count
		));
	}

	private Map<Integer, BigDecimal> listToBigDecimalMap(List<Object[]> results) {
		return results.stream()
				.filter(row -> row[1] != null)
				.collect(Collectors.toMap(
						row -> ((Number) row[0]).intValue(), // ID
						row -> new BigDecimal(row[1].toString()) // Sum
				));
	}

	private Map<Integer, Double> listToAvgMap(List<Object[]> results) {
		return results.stream()
				.filter(row -> row[1] != null)
				.collect(Collectors.toMap(
						row -> ((Number) row[0]).intValue(), // ID
						row -> ((Number) row[1]).doubleValue() // Avg
				));
	}
}
