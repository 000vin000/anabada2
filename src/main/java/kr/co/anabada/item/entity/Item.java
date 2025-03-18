package kr.co.anabada.item.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "item")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemNo")
	private Integer itemNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sellerNo", nullable = false)
	private Seller sellerNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryNo", nullable = false)
	private Item_Category categoryNo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "itemSaleType", nullable = false)
	private ItemSaleType itemSaleType = ItemSaleType.AUCTION;
	
	@Column(name = "itemTitle", length = 50, nullable = false)
	private String itemTitle;
	
	@Column(name = "itemContent", columnDefinition = "text")
	private String itemContent;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "itemStatus", nullable = false)
	private ItemStatus itemStatus = ItemStatus.ACTIVE;

	@Enumerated(EnumType.STRING)
	@Column(name = "itemQuality")
	private ItemQuality itemQuality;
	
	@Column(name = "itemQuantity", nullable = false)
	private Integer itemQuantity = 1;
	
	@Column(name = "itemPrice", nullable = false)
	private Long itemPrice = 0L;
	
	@Column(name = "itemLatitude", nullable = false)
	private Double itemLatitude; // 위도
	
	@Column(name = "itemLongitude", nullable = false)
	private Double itemLongitude; // 경도
	
	@Column(name = "itemStartDate")
	private LocalDateTime itemStartDate;
	
	@Column(name = "itemEndDate")
	private LocalDateTime itemEndDate;
	
	@Column(name = "itemViewCnt", nullable = false)
	private Integer itemViewCnt = 0; // 조회수
	
	@Column(name = "itemAvgRating")
	private Double itemAvgRating; // 평균 평점
	
	@CreationTimestamp
	@Column(name = "itemCreateDate", nullable = false, updatable = false)
	private LocalDateTime itemCreatedDate;
	
	@UpdateTimestamp
	@Column(name = "itemUpdatedDate", nullable = false)
	private LocalDateTime itemUpdatedDate;
	
	
	
	public enum ItemSaleType {
		AUCTION,
		SHOP,
		EXCHANGE,
		DONATION
	}
	
	public enum ItemStatus {
		ACTIVE,
		EXPIRED,
		SOLD
	}
	
	public enum ItemQuality {
		LOW,
		MEDIUM,
		HIGH
	}
	
	// 기본값 설정
	@PrePersist
	public void prePersist() {
		if (itemStartDate == null) {
			itemStartDate = itemCreatedDate;
		}
		if (itemEndDate == null) {
			itemEndDate = itemStartDate.plusDays(3);
		}
	}
}
