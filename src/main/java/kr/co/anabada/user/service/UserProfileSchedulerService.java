package kr.co.anabada.user.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.user.repository.BuyerRepository;
import kr.co.anabada.user.repository.SellerRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserProfileSchedulerService {
	@Autowired
	private SellerRepository sellerRepository;
	@Autowired
	private BuyerRepository buyerRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateSingleSellerStatistics(
			Integer sellerNo,
			Map<Integer, Integer> activeItemCounts,
			Map<Integer, Integer> completedSellItemCounts,
			Map<Integer, Double> avgRatings,
			Map<Integer, BigDecimal> totalSales) {

		int activeCount = activeItemCounts.getOrDefault(sellerNo, 0);
		int completedCount = completedSellItemCounts.getOrDefault(sellerNo, 0);
		double avgRating = avgRatings.getOrDefault(sellerNo, 0.0);
		BigDecimal totalSaleAmt = totalSales.getOrDefault(sellerNo, BigDecimal.ZERO);

		int updatedRows = sellerRepository.updateDailySellerStats(
				sellerNo, activeCount, completedCount, avgRating, totalSaleAmt);

		if (updatedRows > 0) {
			log.debug("Successfully updated statistics for sellerNo {}", sellerNo);
		} else {
			log.warn("No rows updated for sellerNo {}. Seller might not exist.", sellerNo);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateSingleBuyerStatistics(
			Integer buyerNo,
			int bidCount,
			int activeBidItemCount,
			int bidItemCount,
			int bidSuccessCount,
			int paySuccessCount,
			double bidSuccessRate,
			double paySuccessRate) {
		int updatedRows = buyerRepository.updateDailyBuyerStats(
				buyerNo,
				bidCount,
				activeBidItemCount,
				bidItemCount,
				bidSuccessCount,
				paySuccessCount,
				bidSuccessRate,
				paySuccessRate);

		if (updatedRows > 0) {
			log.debug("Successfully updated daily statistics for buyerNo {}", buyerNo);
		} else {
			log.warn("No daily statistics rows updated for buyerNo {}. Buyer might not exist or data unchanged.",
					buyerNo);
		}
	}
}
