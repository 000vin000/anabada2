package kr.co.anabada.sell.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer shipNo;
	@OneToOne
	@JoinColumn(name = "orderNo", nullable = false)
	private Order order;
	@OneToOne
	@JoinColumn(name = "sellerNo", nullable = false)
	private Seller seller;
	@OneToOne
	@JoinColumn(name = "buyerNo", nullable = false)
	private Buyer buyer;
	@Column(nullable = false, columnDefinition = "DEFAULT 'waiting'")
	private String shipStatus;
	private LocalDateTime shipStartDate;
	private LocalDateTime shipEndDate;
	@CreatedDate
	private LocalDateTime shipCreatedDate;
}
// jhu