package kr.co.anabada.user.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UserProfileDashboardDTO {
	private SellerDashboardDTO sellerDashboardDTO;
	private BuyerDashboardDTO buyerDashboardDTO;

	@Data
	@Builder
	public static class SellerDashboardDTO {
		private LocalDateTime sellerUpdatedDate;
		
		// daily update
		private int itemCount; // 판매한 아이템 갯수
		private int activeItemCount; // 판매중 아이템 갯수
		private int completedSellItemCount; // 판매완료 아이템 갯수
		private BigDecimal totalSales; // 누적 총 판매액
		private double avgRating; // 평균 평점
		private double salesSuccessRate; // 판매 성공률
		
		// monthly update
		private String grade; // 판매자 등급
		
		public static SellerDashboardDTO fromEntity(Seller seller) {
			if (seller == null) return null;
			return builder()
					.itemCount(seller.getSellerItemCnt())
					.activeItemCount(seller.getSellerActiveItemCnt())
					.completedSellItemCount(seller.getSellerCompletedSellItemCnt())
					.totalSales(seller.getSellerTotalSales())
					.avgRating(seller.getSellerAvgRating())
					.salesSuccessRate(seller.getSellerSalesSuccessRate())
					.grade(seller.getSellerGrade().getKorean())
					.sellerUpdatedDate(seller.getSellerUpdatedDate())
					.build();
		}
	}

	@Data
	@Builder
	public static class BuyerDashboardDTO {
		private LocalDateTime buyerUpdatedDate;
		
		// daily update
		private int bidCount; // 입찰 횟수
		private int activeBidItemCount; // 입찰중 아이템 개수
		private int bidItemCount; // 입찰한 아이템 개수
		private int bidSuccessCount; // 낙찰 횟수
		private int paySuccessCount; // 결제완료 횟수
		private double bidSuccessRate; // 낙찰률
		private double paySuccessRate; // 결제완료율
		
		public static BuyerDashboardDTO fromEntity(Buyer buyer) {
			if (buyer == null) return null;
			return builder()
					.bidCount(buyer.getBuyerBidCnt())
					.activeBidItemCount(buyer.getBuyerActiveBidItemCnt())
					.bidItemCount(buyer.getBuyerBidItemCnt())
					.bidSuccessCount(buyer.getBuyerBidSuccessCnt())
					.paySuccessCount(buyer.getBuyerPaySuccessCnt())
					.bidSuccessRate(buyer.getBuyerBidSuccessRate())
					.paySuccessRate(buyer.getBuyerPaySuccessRate())
					.buyerUpdatedDate(buyer.getBuyerUpdatedDate())
					.build();
		}
	}

	public static UserProfileDashboardDTO fromEntity(
			SellerDashboardDTO sellerDashboard, BuyerDashboardDTO buyerDashboard) {
		if (sellerDashboard == null || buyerDashboard == null) return null;
		return builder()
				.sellerDashboardDTO(sellerDashboard)
				.buyerDashboardDTO(buyerDashboard)
				.build();
	}
}
