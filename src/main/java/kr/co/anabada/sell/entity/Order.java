package kr.co.anabada.sell.entity;

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
@Table(name = "`order`")
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
	@Column(nullable = false, columnDefinition = "ENUM('pending','paid') DEFAULT 'pending'")
	private String orderStatus;
	@Column(nullable = false, columnDefinition = "DEFAULT '1'")
	private Integer orderAmount;
	@Column(nullable = false, columnDefinition = "DEFAULT '0'")
	private Integer ordershipFee;
	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime orderDate;
	private LocalDateTime orderPayDeadline;
}
// jhu