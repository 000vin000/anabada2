package kr.co.anabada.item.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
// jhu