package kr.co.anabada.admin.entity;

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
import kr.co.anabada.item.entity.Item;
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
public class AdminFee { // 관리자 판매 수수료 기록
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer admincoinNo;
	
	@ManyToOne
	@JoinColumn(name = "itemNo")
	private Item itemNo;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal admincoinAmount;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime admincoinAt;
}
