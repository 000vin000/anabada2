package kr.co.anabada.item.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemDetailDTO {
	private Integer itemNo;
	private String itemSaleType;
	private String itemTitle;
	private String itemContent;	
	private ItemStatus itemStatus;
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
	
	private Integer sellerNo;
	private String sellerNick;
	private String categoryName;
	private int imageCount;
	
	public static ItemDetailDTO fromEntity(Item item, int imageCount) {
		if (item == null) return null;
		return builder()
				.itemNo(item.getItemNo())
				.itemSaleType(item.getItemSaleType().getKorean())
				.itemTitle(item.getItemTitle())
				.itemContent(item.getItemContent())
				.itemStatus(item.getItemStatus())
				.itemQuality(item.getItemQuality() != null ? item.getItemQuality().getKorean() : null)
				.itemQuantity(item.getItemQuantity())
				.itemPrice(item.getItemPrice())
				.itemViewCnt(item.getItemViewCnt())
				.itemAvgRating(item.getItemAvgRating())
				.itemLatitude(item.getItemLatitude())
				.itemLongitude(item.getItemLongitude())
				.itemPurcConfirmed(item.isItemPurcConfirmed())
				.itemSaleConfirmed(item.isItemSaleConfirmed())
				.itemSaleStartDate(item.getItemSaleStartDate())
				.itemSaleEndDate(item.getItemSaleEndDate())
				.itemResvStartDate(item.getItemResvStartDate())
				.itemResvEndDate(item.getItemResvEndDate())
				.itemCreatedDate(item.getItemCreatedDate())
				.itemUpdatedDate(item.getItemUpdatedDate())
				.sellerNo(item.getSeller().getUser().getUserNo())
				.sellerNick(item.getSeller().getUser().getUserNick())
				.categoryName(item.getCategory().getCategoryName())
				.imageCount(imageCount).build();
	}
	
	public String getFormattedDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return date.format(formatter);
    }
	
    public String getFormattedPrice(BigDecimal price) {
        if (price == null) return "0";
        return NumberFormat.getInstance().format(price);
    }
}
