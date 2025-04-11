package kr.co.anabada.user.scheduler;

import java.math.BigDecimal;
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
import kr.co.anabada.user.service.UserProfileSchedulerService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserProfileScheduler {
	@Autowired
	private ItemDetailRepository itemDetailRepository;
	@Autowired
	private UserProfileSchedulerService newUserProfileSchedulerService;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private BidRepository bidRepository;

	@Scheduled(cron = "0 0 2 * * *")
	public void updateDailyStatistics() {
		log.info("Starting daily statistics update job...");

		// Seller--------------------------
		Map<Integer, Integer> activeItemCounts = listToMap(
				itemDetailRepository.countItemsPerSellerByItemStatus(ItemStatus.ACTIVE));
		Map<Integer, Integer> completedSellItemCounts = listToMap(
				paymentRepository.countPaysPerSellerByPayStatus(PayStatus.PAID));
		Map<Integer, Double> avgRatings = listToAvgMap(
				reviewRepository.averageReviewRatingPerSeller());
		Map<Integer, BigDecimal> totalSales = listToBigDecimalMap(
				paymentRepository.sumSalesPerSellerByPayStatus(PayStatus.PAID));
		
		log.info(activeItemCounts.toString());
		log.info(completedSellItemCounts.toString());
		log.info(avgRatings.toString());
		log.info(totalSales.toString());

		Set<Integer> allSellerNos = new HashSet<>();
		allSellerNos.addAll(activeItemCounts.keySet());
		allSellerNos.addAll(completedSellItemCounts.keySet());
		allSellerNos.addAll(avgRatings.keySet());
		allSellerNos.addAll(totalSales.keySet());

		log.info("Found {} sellers to update.", allSellerNos.size());

		int successSellerCount = 0;
		int failSellerCount = 0;
		for (Integer sellerNo : allSellerNos) {
			try {
				newUserProfileSchedulerService.updateSingleSellerStatistics(
						sellerNo, activeItemCounts, completedSellItemCounts, avgRatings, totalSales);
				successSellerCount++;
			} catch (Exception e) {
				log.error("Failed to update statistics for sellerNo {}: {}", sellerNo, e.getMessage(), e);
			}
			log.info("Daily statistics update job finished. Success: {}, Fail: {}", successSellerCount, failSellerCount);
		}

		// Buyer---------------------------
		Map<Integer, Integer> bidCounts = listToMap(
				bidRepository.countAllBidsPerBuyer());
		Map<Integer, Integer> activeBidItemCounts = listToMap(
				itemDetailRepository.countItemsPerBuyerByItemStatusAndOptionalBidStatus(ItemStatus.ACTIVE, BidStatus.ACTIVE));
		Map<Integer, Integer> bidItemCounts = listToMap(
				itemDetailRepository.countItemsPerBuyerByItemStatusAndOptionalBidStatus(ItemStatus.ACTIVE, null));
		Map<Integer, Integer> bidSuccessCounts = listToMap(
				bidRepository.countBidsPerBuyerByBidStatus(BidStatus.WINNING));
		Map<Integer, Integer> paySuccessCounts = listToMap(
				paymentRepository.countPaysPerBuyerByPayStatus(PayStatus.PAID));
		
		log.info(bidCounts.toString());
		log.info(activeBidItemCounts.toString());
		log.info(bidItemCounts.toString());
		log.info(bidSuccessCounts.toString());
		log.info(paySuccessCounts.toString());

		Set<Integer> allBuyerNos = new HashSet<>();
		allBuyerNos.addAll(bidCounts.keySet());
		allBuyerNos.addAll(activeBidItemCounts.keySet());
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

		        double bidSuccessRate = calculateRate(bidSuccessCount, bidCount);
		        double paySuccessRate = calculateRate(paySuccessCount, bidSuccessCount);

		        newUserProfileSchedulerService.updateSingleBuyerStatistics(
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
		log.info("Buyer daily statistics update finished. Success: {}, Fail: {}", successBuyerCount, failBuyerCount);
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
