package kr.co.anabada.user.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
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
	private List<ItemSummaryDTO> sellSummaryDTOs;

	private int buyerBidCnt;
	private List<ItemSummaryDTO> buySummaryDTOs;

//	private List<ReviewSummaryDTO> recentReviews;
	
	@Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemSummaryDTO {
        private Integer itemNo;
        private String itemTitle;
        private BigDecimal itemPrice;
        private String itemStatus;
        private LocalDateTime itemSoldDate;
        
        public String getFormattedDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            return itemSoldDate.format(formatter);
        }
    	
        public String getFormattedPrice() {
            if (itemPrice == null) return "0";
            return NumberFormat.getInstance().format(itemPrice);
        }
	}
    
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return userCreatedDate.format(formatter);
    }
}
