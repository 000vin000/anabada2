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
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account { // 현금 입출금 내역
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accountNo;
	
	@ManyToOne
	@JoinColumn(name = "userNo", nullable = false)
	private User UserNo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountType accountType = AccountType.DEPOSIT;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal accountAmount;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal accountBalance;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PayType accountPayType = PayType.NOPASSBOOK;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime accountAt;
	
	public enum PayType {
		NOPASSBOOK("무통장입금"),
		CARD("카드");
		
		private final String korean;
	       
		PayType(String korean) {
			this.korean = korean;
		}
	       
		public String getKorean() {
			return korean;
		}
	}
	
	public enum AccountType {
		DEPOSIT("입금"),
		WITHDRAWAL("출금");
		
		private final String korean;
	       
		AccountType(String korean) {
			this.korean = korean;
		}
	       
		public String getKorean() {
			return korean;
		}
	}
}


