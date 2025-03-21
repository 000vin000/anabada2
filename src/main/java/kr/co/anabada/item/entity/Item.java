package kr.co.anabada.item.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	private ItemSaleType itemSaleType = ItemSaleType.AUCTION;
	
	@Column(length = 50, nullable = false)
	private String itemTitle;
	
	@Lob
	private String itemContent;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ItemStatus itemStatus = ItemStatus.ACTIVE;

	@Enumerated(EnumType.STRING)
	private ItemQuality itemQuality;
	
	@Column(nullable = false)
	private Integer itemQuantity = 1;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal itemPrice = BigDecimal.ZERO;
	
	@Column(nullable = false)
	private Double itemLatitude;
	
	@Column(nullable = false)
	private Double itemLongitude;
	
	@Column(nullable = false)
	private Integer itemViewCnt = 0;
	
	private Double itemAvgRating;
	
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
	

	
	@PrePersist
	public void prePersist() {
		if (itemSaleStartDate == null) {
			itemSaleStartDate = itemCreatedDate;
		}
		if (itemSaleEndDate == null) {
			itemSaleEndDate = itemSaleStartDate.plusDays(3);
		}
	}
	
	public enum ItemSaleType {
		AUCTION("경매"),
		SHOP("쇼핑몰"),
		EXCHANGE("교환"),
		DONATION("나눔");
		
		private final String korean;
	    
	    ItemSaleType(String korean) {
	        this.korean = korean;
	    }
	    
	    public String getKorean() {
	        return korean;
	    }
	}
	
	public enum ItemStatus {
		WAITING("대기중"),
		ACTIVE("판매중"),
		EXPIRED("종료"),
		RESERVED("예약중"),
		SOLD("판매완료");
		
		private final String korean;
	    
	    ItemStatus(String korean) {
	        this.korean = korean;
	    }
	    
	    public String getKorean() {
	        return korean;
	    }
	}
	
	public enum ItemQuality {
		LOW("하"),
		MEDIUM("중"),
		HIGH("상");
		
		private final String korean;
	    
	    ItemQuality(String korean) {
	        this.korean = korean;
	    }
	    
	    public String getKorean() {
	    	return korean;
	    }
	}
}
