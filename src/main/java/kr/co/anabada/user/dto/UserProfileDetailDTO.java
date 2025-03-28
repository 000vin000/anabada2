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
		private int pendingSales;
		private int completedSales;
		private int canceledSales;
		private Map<String, Integer> salesByCategory;
		private List<BigDecimal> monthlyRevenueStats;
		private double salesSuccessRate;
		// 판매자 관련 통계
	}

	@Data
	public static class BuyerDetailDTO {
		private LocalDateTime buyerCreatedDate;
		private LocalDateTime buyerUpdatedDate;
		private int totalBids;
		private int wonBids;
		private int activeBids;
		private Map<String, Integer> purchasesByCategory;
		private List<BigDecimal> monthlySpendingStats;
		private double bidSuccessRate;
		// 구매자 관련 통계
	}
}
