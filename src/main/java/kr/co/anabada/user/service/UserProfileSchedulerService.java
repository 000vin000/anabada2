package kr.co.anabada.user.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.user.entity.Seller.SellerGrade;
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
			int itemCount,
			int activeItemCount,
			int completedSellItemCount,
			BigDecimal totalSales,
			double avgRating,
			double salesSuccessRate) {

		int updatedRows = sellerRepository.updateDailySellerStats(
				sellerNo,
				itemCount,
				activeItemCount,
				completedSellItemCount, totalSales,
				avgRating,
				salesSuccessRate);

		if (updatedRows > 0) {
			log.debug("Successfully updated statistics for sellerNo {}", sellerNo);
		} else {
			log.warn("No rows updated for sellerNo {}", sellerNo);
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
			log.warn("No rows updated for buyerNo {}", buyerNo);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateSingleSellerGrade(Integer sellerNo, SellerGrade sellerGrade) {
		
		int updatedRows = sellerRepository.updateSellerGrade(sellerNo, sellerGrade);
		if (updatedRows > 0) {
			log.debug("Successfully updated statistics for sellerNo {}", sellerNo);
		} else {
			log.warn("No rows updated for sellerNo {}", sellerNo);
		}
	}
}
