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
public class Item {
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
	
	public String addCommas(int num) {
        NumberFormat formatter = NumberFormat.getInstance();
        return formatter.format(num);
    }
	
	public String getItemStatusInKorean() {
	    if (this.itemAuction == null) {
	        return null; 
	    }

	    switch (this.itemAuction.trim().toLowerCase()) {
	        case "waiting":
	            return "대기중";
	        case "bidding":
	            return "입찰중";
	        case "sold":
	            return "판매완료";
	        case "closed":
	            return "종료";
	        default:
	            return "알 수 없음";
	    }
	}
	
	//itemAuction만 변환 필요할때
	public static String getItemStatusInKorean(String itemAuction) {
		Item temp = new Item();
        temp.itemAuction = itemAuction;
        return temp.getItemStatusInKorean();
	}

    public void setItemStatusFromString(String auction) {
        this.itemAuction = auction;
    }
    
    public String getFormattedDate(LocalDateTime time) {
    	return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getStatusStr(String itemStatus) {
    	if (itemStatus.equals("high")) return "상";
    	else if (itemStatus.equals("mid")) return "중";
    	else return "하";
    }
    
    public String getCategoryStr(String itemGender, String itemCate) {
    	String gender = null;
    	String cate = null;
    	
    	if (itemGender.equals("m")) gender = "남성 ";
    	else gender = "여성 ";
    	
    	if (itemCate.equals("top")) cate = "상의";
    	else if (itemCate.equals("bottom")) cate = "하의";
    	else if (itemCate.equals("dress")) cate = "원피스";
    	else if (itemCate.equals("outer")) cate = "아우터";
    	else if (itemCate.equals("etc")) cate = "기타";
    	else cate = "세트상품";
    	
    	return gender + cate;
    }
}
