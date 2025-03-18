package kr.co.anabada.buy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import kr.co.anabada.sell.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer payNo;
	@OneToOne
	@JoinColumn(name = "fk_orderNo")
	@NotNull
	private Order order;
	@NotNull
	private String payStatus;
	private LocalDateTime payCompletedDate;
	private LocalDateTime payCancelledDate;
	@NotNull
	private LocalDateTime payData;
}
// jhu