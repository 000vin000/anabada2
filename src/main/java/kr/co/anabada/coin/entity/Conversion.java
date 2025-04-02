package kr.co.anabada.coin.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
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
import kr.co.anabada.admin.entity.Admin;
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
public class Conversion { // 현금/코인 전환 신청
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer conversionNo;
	
	@ManyToOne
	@JoinColumn(name = "userNo", nullable = false)
	private User userNo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ConversionType conversionType = ConversionType.TOCOIN;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal conversionAmount;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime conversionReqAt;
	
	private LocalDateTime conversionAt;
	
	@ManyToOne
	@JoinColumn(name = "adminNo")
	private Admin adminNo;
	
	public enum ConversionType {
		TOCOIN("코인으로 전환"),
		TOCASH("현금으로 전환");
		
		private final String korean;
	       
		ConversionType(String korean) {
			this.korean = korean;
		}
	       
		public String getKorean() {
			return korean;
		}
	}
}
