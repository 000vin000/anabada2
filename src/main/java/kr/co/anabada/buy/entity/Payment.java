package kr.co.anabada.buy.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kr.co.anabada.sell.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer payNo;
	@OneToOne
	@JoinColumn(name = "orderNo", nullable = false)
	private Order order;
	@Column(nullable = false, columnDefinition = "ENUM('pending','paid','rejected') DEFAULT 'pending'")
	private String payStatus;
	private LocalDateTime payCompletedDate;
	private LocalDateTime payCancelledDate;
	@Builder.Default
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal payPrice = BigDecimal.ZERO;
	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime payDate;
}
// jhu