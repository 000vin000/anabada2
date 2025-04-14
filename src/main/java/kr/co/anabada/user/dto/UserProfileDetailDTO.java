package kr.co.anabada.user.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserProfileDetailDTO extends UserProfileDTO {
	private SellerDetailDTO sellerDetailDTO;
	private BuyerDetailDTO buyerDetailDTO;

	@Data
	public static class SellerDetailDTO {
		private LocalDateTime sellerCreatedDate;
		private LocalDateTime sellerUpdatedDate;
		
		// daily update
		private int activeItemCount; // 판매중 아이템 갯수
		private int completedSellItemCount; // 판매완료 아이템 갯수
		private double avgRating; // 평균 평점
		
		// monthly update
		private String grade; // 판매자 등급
		private BigDecimal monthlySales; // 한달 기준 판매액
		
		// 누적 update
		private BigDecimal totalSales; // 누적 총 판매액
		
		// 실시간 계산
		private Map<String, Integer> sellItemCountByCategory; // 카테고리별 판매아이템 갯수
		private List<BigDecimal> monthlyRevenue; // 월별 판매액
		private double salesSuccessRate; // 판매 성공률
	}

	@Data
	public static class BuyerDetailDTO {
		private LocalDateTime buyerCreatedDate;
		private LocalDateTime buyerUpdatedDate;
		
		// daily update
		private int bidCount; // 입찰 횟수
		private int activeBidItemCount; // 입찰중 아이템 개수
		private int bidItemCount; // 입찰한 아이템 개수
		private int bidSuccessCount; // 낙찰 횟수
		private int paySuccessCount; // 결제완료 횟수
		private double bidSuccessRate; // 낙찰률
		private double paySuccessRate; // 결제완료율
		
		// monthly update
		private BigDecimal monthlySpending; // 한달 기준 지출액

		// 실시간 계산
		private Map<String, Integer> bidItemCountByCategory; // 카테고리별 입찰아이템 갯수
		private List<BigDecimal> monthlySpendingStats; // 월별 지출액
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	public static class AuthenticatedItemSummaryDTO extends ItemSummaryDTO {
		private boolean isReviewed;
	}
}
