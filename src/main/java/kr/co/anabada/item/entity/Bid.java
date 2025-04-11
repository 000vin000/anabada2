package kr.co.anabada.item.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer bidNo;
	@ManyToOne
	@JoinColumn(name = "itemNo", nullable = false)
	private Item item;
	@ManyToOne
	@JoinColumn(name = "userNo", nullable = false)
	private User user;
	@ManyToOne
	@JoinColumn(name = "buyerNo", nullable = false)
	private Buyer buyer;
	@Column(nullable = false)
	private BigDecimal bidPrice;
	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime bidTime;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BidStatus bidStatus = BidStatus.ACTIVE; //java enum 으로 수정했습니다
	
	public enum BidStatus {
		ACTIVE, WINNING, LOST, CANCELLED;
	}
	
	public String formatToKoreanDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        return dateTime.format(formatter);
    }
	
	public String formatBigDecimal(BigDecimal price) {
	    DecimalFormat formatter = new DecimalFormat("#,###");
	    return formatter.format(price);
	}
}
// jhu