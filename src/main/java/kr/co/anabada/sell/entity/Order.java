package kr.co.anabada.sell.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
	@JoinColumn(name = "itemNo", nullable = false)
	private Item item;
	@OneToOne
	@JoinColumn(name = "bidNo")
	private Bid bid;
	@OneToOne
	@JoinColumn(name = "sellerNo", nullable = false)
	private Seller seller;
	@OneToOne
	@JoinColumn(name = "buyerNo", nullable = false)
	private Buyer buyer;
	@Column(nullable = false)
	private String orderStatus;
	@Column(nullable = false)
	private Integer orderAmount;
	@Column(nullable = false)
	private Integer ordershipFee;
	@Column(nullable = false)
	private LocalDateTime orderDate;
	private LocalDateTime orderPayDeadline;
}
// jhu