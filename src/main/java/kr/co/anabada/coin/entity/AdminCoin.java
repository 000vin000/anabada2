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
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCoin { // 관리자 재화 변동 내역
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer admincoinNo;
	
	@ManyToOne
	@JoinColumn(name = "userNo")
	private User userNo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AdminCoinType admincoinType = AdminCoinType.DEPOSIT;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal admincoinAmount;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal admincoinBalance;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime admincoinAt;
	
	@ManyToOne
	@JoinColumn(name = "adminNo")
	private Admin admincoinAdmin;
	
	public enum AdminCoinType {
		BID("입찰"),
		CANCEL("입찰취소"),
		WINNING("낙찰"),
		CHARGE("수수료"),
		DEPOSIT("입금"),
		WITHDRAWAL("출금");
		
		private final String korean;
	       
		AdminCoinType(String korean) {
			this.korean = korean;
		}
	       
		public String getKorean() {
			return korean;
		}
	}
}
