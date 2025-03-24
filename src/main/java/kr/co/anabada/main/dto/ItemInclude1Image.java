package kr.co.anabada.main.dto;

import java.time.Duration;
import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Immutable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInclude1Image {
	private Integer item_no;
	private String item_title;
	private Integer item_price;
	private String user_nick;
	private String item_quality;
	private LocalDateTime item_sale_start_date;
	private LocalDateTime item_sale_end_date;
	private Double item_latitude;
	private Double item_longitude;
	private Integer item_view_cnt;
	private byte[] image_file;
	private String category_no;
	
	private String base64Image;
	
	// 경매까지 남은 시간
	public String remainingTime(LocalDateTime date) {
	    LocalDateTime now = LocalDateTime.now();
	    
	    // 종료 시간이 이미 지난 경우
	    if (date.isBefore(now)) {
	        return "경매 종료";
	    }

	    Duration duration = Duration.between(now, date);

	    long days = duration.toDays();
	    long hours = duration.toHoursPart();
	    long minutes = duration.toMinutesPart();

	    return String.format("%d일 %d시간 %d분", days, hours, minutes);
	}

}
