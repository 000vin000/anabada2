package kr.co.anabada.user.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.dto.UserProfileDashboardDTO.BuyerDashboardDTO;
import kr.co.anabada.user.dto.UserProfileDashboardDTO.SellerDashboardDTO;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
	private Integer userNo;
	private String userId;
	private String userNick;
//	private String profileImage;
	private LocalDateTime userCreatedDate;
//	private int trustScore;

	private int sellerItemCnt;
	private double sellerAvgRating;
	private String sellerGrade;

	private int buyerBidCnt;
	
	public static UserProfileDTO fromEntity(User user, Seller seller, Buyer buyer) {
		if (seller == null || buyer == null) return null;
		return builder()
				.userNo(user.getUserNo())
				.userId(user.getUserId())
				.userNick(user.getUserNick())
				.userCreatedDate(user.getUserCreatedDate())
				.sellerItemCnt(seller.getSellerActiveItemCnt())
				.sellerAvgRating(seller.getSellerAvgRating())
				.sellerGrade(seller.getSellerGrade().getKorean())
				.buyerBidCnt(buyer.getBuyerBidItemCnt())
				.build();
	}
    
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return userCreatedDate.format(formatter);
    }
	
	@Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class ItemSummaryDTO {
        private Integer itemNo;
        private String itemTitle;
        private BigDecimal itemPrice;
        private String itemStatus;
        private LocalDateTime itemSoldDate;
        private int viewCount;
        private int bidCount;
        
        public String getFormattedDate() {
        	if (itemSoldDate == null) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            return itemSoldDate.format(formatter);
        }
    	
        public String getFormattedPrice() {
            if (itemPrice == null) return "0";
            return NumberFormat.getInstance().format(itemPrice);
        }

		public static ItemSummaryDTO fromEntity(Item item, int bidCount) {
			if (item == null) return null;
			return builder()
					.itemNo(item.getItemNo())
					.itemTitle(item.getItemTitle())
					.itemPrice(item.getItemPrice())
					.itemStatus(item.getItemStatus().getKorean())
					.itemSoldDate(item.getItemSoldDate())
					.viewCount(item.getItemViewCnt())
					.bidCount(bidCount)
					.build();
		}
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
