package kr.co.anabada.sell.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import kr.co.anabada.buy.entity.Bid;
import kr.co.anabada.item.entity.Item;
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
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer orderNo;
	@OneToOne
	@JoinColumn(name = "fk_itemNo")
	@NotNull
	private Item item;
	@OneToOne
	@JoinColumn(name = "fk_bidNo")
	private Bid bid;
	@OneToOne
	@JoinColumn(name = "fk_sellerNo")
	@NotNull
	private Seller seller;
	@OneToOne
	@JoinColumn(name = "fk_buyerNo")
	private Buyer buyer;
	@NotNull
	private String orderStatus;
	@NotNull
	private Integer orderAmount;
	@NotNull
	private Integer ordershipFee;
	@NotNull
	private LocalDateTime orderDate;
	private LocalDateTime orderPayDeadline;
}
// jhu