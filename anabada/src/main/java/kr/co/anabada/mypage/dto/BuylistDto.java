package kr.co.anabada.mypage.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuylistDto {
	private int userNo;
	private int itemNo;
	private String itemName;
	private byte[] imageFile;
	private String itemAuction;
	private int bidPrice;
	private int itemPrice;
	private LocalDateTime bidTime;
	private LocalDateTime itemEnd;
	
	public String getImageBase64() {
        if (imageFile != null && imageFile.length > 0) {
            return Base64.getEncoder().encodeToString(imageFile);
        }
        return null;
    }
	
	public String getItemAuctionName() {
	    if (itemAuction == null) {
	    	return "unknown";
	    }
	    
	    switch(itemAuction) {
		case "waiting":
			return "대기";
		case "bidding":
			return "입찰 가능";
		case "sold":
			return "낙찰";
		default:
			return "unknown";
		}
	}
	
	public String getFormattedBidTime() {
        if (bidTime == null) {
            return "";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
        return bidTime.format(formatter);
    }
	
	public String getFormattedItemEnd() {
        if (itemEnd == null) {
            return "";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
        return itemEnd.format(formatter);
    }
}
