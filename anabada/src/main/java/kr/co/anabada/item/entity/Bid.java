package kr.co.anabada.item.entity;

import java.text.NumberFormat;
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
public class Bid {
	private int bidNo;
	private int itemNo;
	private int userNo;
	private int bidPrice;
	private LocalDateTime bidTime;
	private String userNick;
	
	public String getTimeStr(LocalDateTime bidTime) {
		String convertedDate = bidTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		return convertedDate;
	}
	
	public String addCommas(int num) {
        NumberFormat formatter = NumberFormat.getInstance();
        return formatter.format(num);
    }
}
