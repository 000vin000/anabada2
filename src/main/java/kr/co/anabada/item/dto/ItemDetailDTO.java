package kr.co.anabada.item.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailDTO {
	private Integer itemNo;
	private String itemSaleType;
	private String itemTitle;
	private String itemContent;	
	private String itemStatus;
	private String itemQuality;	
	private Integer itemQuantity;
	private BigDecimal itemPrice;	
	private Integer itemViewCnt;
	private Double itemAvgRating;
	
	private Double itemLatitude;	
	private Double itemLongitude;
	
	private boolean itemPurcConfirmed;
	private boolean itemSaleConfirmed;
	
	private LocalDateTime itemSaleStartDate;
	private LocalDateTime itemSaleEndDate;
	private LocalDateTime itemResvStartDate;
	private LocalDateTime itemResvEndDate;
	private LocalDateTime itemCreatedDate;
	private LocalDateTime itemUpdatedDate;
	
	private Integer sellerNo; //seller
	private String sellerNick;
	private String categoryName; //category
	private BigDecimal pointBalance; //point_account
	private int imageCount; //image
//	private List<Review> reviews;
	
	
	
	public String getFormattedDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return date.format(formatter);
    }
	
	public String addCommas(Integer num) {
        NumberFormat formatter = NumberFormat.getInstance();
        return formatter.format(num);
    }
}
