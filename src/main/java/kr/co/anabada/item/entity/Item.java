package kr.co.anabada.item.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "item")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemNo;
	
	@ManyToOne
	@JoinColumn(name = "fk_sellerNo")
	private Seller sellerNo;
	
	@OneToOne
	@JoinColumn(name = "fk_categoryNo", nullable = false)
	private Item_Category categoryNo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "itemSaleType", nullable = false)
	private ItemSaleType itemSaleType = ItemSaleType.auction;
	
	@Column(name = "itemTitle", length = 50, nullable = false)
	private String itemTitle;
	
	@Column(name = "itemContent", columnDefinition = "text")
	private String itemContent;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "itemStatus", nullable = false)
	private ItemStatus itemStatus = ItemStatus.active;
	
	@Column(name = "itemQuality")
	private Integer itemQuality;
	
	@Column(name = "itemQuantity", nullable = false)
	private Integer itemQuantity;
	
	@Column(name = "itemPrice")
	private Integer itemPrice;
	
	@Column(name = "itemLatitude", nullable = false)
	private Double itemLatitude; // 위도
	
	@Column(name = "itemLongitude", nullable = false)
	private Double itemLongitude; // 경도
	
	@CreationTimestamp
	@Column(name = "itemCreateDate")
	private LocalDateTime itemCreatedDate;
	
	@Column(name = "itemStartDate")
	private LocalDateTime itemStartDate;
	
	@Column(name = "itemEndDate")
	private LocalDateTime itemEndDate;
	
	@UpdateTimestamp
	private LocalDateTime itemUpdatedDate;
	
	@Column(name = "itemViewCnt", nullable = false)
	private Integer itemViewCnt; // 조회수
	
	@Column(name = "itemAvgRating")
	private Double itemAvgRating; // 평균 평점
	
	public enum ItemSaleType {
		auction, shop, exchange, donation
	}
	
	public enum ItemStatus {
		active, expired, sold
	}
	
	// 기본값 설정
	@PrePersist
	public void prePersist() {
		if (itemQuantity == null) { itemQuality = 1; }
		if (itemPrice == null) { itemPrice = 0; }
		if (itemViewCnt == null) { itemViewCnt = 0; }
		if (itemStartDate == null) { itemStartDate = itemCreatedDate; }
		if (itemEndDate == null) { itemEndDate = itemStartDate.plusDays(3); }
	}
}
