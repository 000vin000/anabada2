package kr.co.anabada.sell.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
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
	@JoinColumn(name = "orderNo")
	@NotNull
	private Order order;
	@OneToOne
	@JoinColumn(name = "sellerNo")
	@NotNull
	private Seller seller;
	@OneToOne
	@JoinColumn(name = "buyerNo")
	@NotNull
	private Buyer buyer;
	@NotNull
	private String shipStatus;
	private LocalDateTime shipDate;
	private LocalDateTime shipStartDate;
	private LocalDateTime shipEndDate;
}
// jhu