package kr.co.anabada.coin.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCoin { // 유저 코인 변동 내역
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer changecoinNo;
	
	@ManyToOne
	@JoinColumn(name = "userNo", nullable = false)
	private User userNo;
	
	@ManyToOne
	@JoinColumn(name = "itemNo", nullable = false)
	private Item itemNo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChangeCoinType changecoinType = ChangeCoinType.BID;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal changecoinAmount;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal changecoinBalance;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime changecoinAt;
	
	public enum ChangeCoinType {
		BID("입찰"),
		CANCEL("입찰취소"),
		WINNING("낙찰"),
		CHARGE("수수료");
		
		private final String korean;
	       
		ChangeCoinType(String korean) {
			this.korean = korean;
		}
	       
		public String getKorean() {
			return korean;
		}
	}
}
