package kr.co.anabada.item.entity;

import java.text.NumberFormat;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemNo;
	
	@ManyToOne
	@JoinColumn(name = "sellerNo", nullable = false)
	private Seller seller;
	
	@ManyToOne
	@JoinColumn(name = "categoryNo", nullable = false)
	private Item_Category category;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ItemSaleType itemSaleType = ItemSaleType.auction;
	
	@Column(length = 50, nullable = false)
	private String itemTitle;
	
	@Lob
	private String itemContent;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ItemStatus itemStatus = ItemStatus.active;

	@Enumerated(EnumType.STRING)
	private ItemQuality itemQuality;
	
	@Column(nullable = false)
	private Integer itemQuantity = 1;
	
	@Column(nullable = false)
	private Long itemPrice = 0L;
	
	@Column(nullable = false)
	private Double itemLatitude; // 위도
	
	@Column(nullable = false)
	private Double itemLongitude; // 경도
	
	@Column(nullable = false)
	private Integer itemViewCnt = 0; // 조회수
	
	private Double itemAvgRating; // 평균 평점
	
	private LocalDateTime itemSaleStartDate;
	
	private LocalDateTime itemSaleEndDate;

	private LocalDateTime itemResvStartDate;
	
	private LocalDateTime itemResvEndDate;
	
	@Column(nullable = false)
	private boolean itemPurcConfirmed = false;

	@Column(nullable = false)
	private boolean itemSaleConfirmed = false;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime itemCreatedDate;
	
	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime itemUpdatedDate;
	
	public enum ItemSaleType {
		auction,
		shop,
		exchange,
		donation
	}
	
	public enum ItemStatus {
		active,
		expired,
		sold
	}
	
	public enum ItemQuality {
		LOW,
		MEDIUM,
		HIGH
	}
	
	public String addCommas(Integer num) {
        NumberFormat formatter = NumberFormat.getInstance();
        return formatter.format(num);
    }
	
	// 기본값 설정
	@PrePersist
	public void prePersist() {
		if (itemSaleStartDate == null) {
			itemSaleStartDate = itemCreatedDate;
		}
		if (itemSaleEndDate == null) {
			itemSaleEndDate = itemSaleStartDate.plusDays(3);
		}
	}
}
