package kr.co.anabada.item.entity;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImage {
	private int itemNo;
	private int userNo;
	private String itemGender;
	private String itemCate; //ENUM 변경으로 컬럼명 itemCate > itemCate, 값 int >String
	private String itemAuction; //ENUM 변경으로 값 int >String
	private LocalDateTime itemStart;
	private LocalDateTime itemEnd;
	private int itemPrice;
	private String itemName;
	private String itemContent;
	private String itemStatus;
	private String base64Image;
	private String userNick;	// 유저 닉네임
	private int bidCount;	// 입찰 횟수
	
	public String addCommas(int num) {
        NumberFormat formatter = NumberFormat.getInstance();
        return formatter.format(num);
    }
	
	public String getItemAuctionStr(String itemAuction) {
		if (itemAuction.equals("waiting")) {
			return getStartTime(this.itemStart);
		} else if (itemAuction.equals("bidding")) {
			return getCountDown(this.itemEnd);
		} else {
			return "마감됨";
		}
	}
	
	public String getCountDown(LocalDateTime itemEnd) {
		LocalDateTime now = LocalDateTime.now();
		Duration countdown = Duration.between(now, itemEnd);
		
		long minute = countdown.getSeconds() / 60;
		
		long hour = minute / 60;
		minute = minute % 60;
		
		long day = hour / 24;
		hour = hour % 24;
		
		return day + "일 " + hour +"시간 " + minute + "분";
	}
	
	public String getStartTime(LocalDateTime itemStart) {
		return itemStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm 오픈"));
	}
}