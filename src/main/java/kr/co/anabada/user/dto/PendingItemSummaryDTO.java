package kr.co.anabada.user.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kr.co.anabada.item.entity.Item;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class PendingItemSummaryDTO {
    private Integer itemNo;
    private String itemTitle;
    private BigDecimal itemFinalBidPrice;
    private LocalDateTime itemResvEndDate;
    private String itemCounterNick; // 판매자=>구매자, 구매자=>판매자
    
	public static PendingItemSummaryDTO fromEntity(UserRole userRole, Item item) {
		if (item == null) return null;
		
		String counterNick = "알 수 없음";
		try {
			counterNick = (userRole == UserRole.SELLER)
					? item.getBuyer().getUser().getUserNick()
					: item.getSeller().getUser().getUserNick();
		} catch (NullPointerException e) {
			log.error(((userRole == UserRole.SELLER) ? "구매자" : "판매자") + " 정보를 불러올 수 없습니다.");
		}
		
		return builder()
				.itemNo(item.getItemNo())
				.itemTitle(item.getItemTitle())
				.itemFinalBidPrice(item.getItemPrice())
				.itemResvEndDate(item.getItemResvEndDate())
				.itemCounterNick(counterNick)
				.build();
	}

    public String getFormattedFinalPrice() {
        if (itemFinalBidPrice == null) return "0";
        return NumberFormat.getInstance().format(itemFinalBidPrice);
    }

    public String getFormattedResvEndDate() {
        if (itemResvEndDate == null) return "마감 기한 없음";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm '까지'");
        return itemResvEndDate.format(formatter);
    }
    
    public enum UserRole {
    	SELLER, BUYER
    }
}
