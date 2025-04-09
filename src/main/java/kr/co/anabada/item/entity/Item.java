package kr.co.anabada.item.entity;

import java.math.BigDecimal;
import java.time.Duration;
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
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemNo;

    @ManyToOne
    @JoinColumn(name = "seller_no", nullable = false)
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

	@Setter
	@Column(nullable = false)
	private Double itemLatitude;

	@Setter
	@Column(nullable = false)
	private Double itemLongitude;

	@Column(nullable = false)
	private Integer itemViewCnt = 0;

	private Double itemAvgRating;

	private LocalDateTime itemSaleStartDate;

	private LocalDateTime itemSaleEndDate;

	private LocalDateTime itemResvStartDate;

	private LocalDateTime itemResvEndDate;
	
	private LocalDateTime itemSoldDate;

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
			itemSaleStartDate = LocalDateTime.now();
		}
		if (itemSaleEndDate == null) {
			itemSaleEndDate = itemSaleStartDate.plusDays(3);
		}
	}

	public void activate() {
		if (this.itemStatus == ItemStatus.WAITING) {
			this.itemStatus = ItemStatus.ACTIVE;
		} else {
			throw new IllegalStateException("판매 상태로 변경할 수 없습니다: " + this.itemStatus);
		}
	}

	public void expire() {
		if (this.itemStatus == ItemStatus.ACTIVE) {
			this.itemStatus = ItemStatus.EXPIRED;
		} else {
			throw new IllegalStateException("종료 상태로 변경할 수 없습니다: " + this.itemStatus);
		}
	}

	public void reserve(LocalDateTime resvStartDate, LocalDateTime resvEndDate) {
		if (this.itemStatus == ItemStatus.ACTIVE) {
			this.itemResvStartDate = resvStartDate;
			this.itemResvEndDate = resvEndDate;
			this.itemStatus = ItemStatus.RESERVED;
		} else {
			throw new IllegalStateException("예약 상태로 변경할 수 없습니다: " + this.itemStatus);
		}
	}

	public void markAsSold() {
		if (this.itemPurcConfirmed == true && this.itemSaleConfirmed == true) {
			this.itemStatus = ItemStatus.SOLD;
			this.itemSoldDate = LocalDateTime.now();
		} else {
			throw new IllegalStateException("판매완료 상태로 변경할 수 없습니다: " + this.itemStatus);
		}
	}

	public boolean isAuction() {
		return this.itemSaleType == ItemSaleType.AUCTION;
	}

	public boolean isExpired() {
		return this.itemStatus == ItemStatus.EXPIRED && LocalDateTime.now().isAfter(this.itemSaleEndDate);
	}
	
	public boolean isWaiting() {
		return this.itemStatus == ItemStatus.WAITING && LocalDateTime.now().isBefore(itemSaleStartDate);
	}

	public boolean isActive() {
		return this.itemStatus == ItemStatus.ACTIVE && !isExpired() && LocalDateTime.now().isAfter(itemSaleStartDate);
	}

	public Duration getTimeLeft() {
		if (isWaiting()) {
			return Duration.between(LocalDateTime.now(), this.itemSaleStartDate);
		}
		if (isActive()) {
			return Duration.between(LocalDateTime.now(), this.itemSaleEndDate);
		}
		return Duration.ZERO;
	}

	public boolean canBid(BigDecimal bidAmount) {
		return isAuction() && isActive() && bidAmount.compareTo(this.itemPrice.add(new BigDecimal(999))) > 0;
	}

	public boolean isFullyConfirmed() {
		return this.itemPurcConfirmed && this.itemSaleConfirmed;
	}

	public enum ItemSaleType {
		AUCTION("경매"), SHOP("쇼핑몰"), EXCHANGE("교환"), DONATION("나눔");

		private final String korean;

		ItemSaleType(String korean) {
			this.korean = korean;
		}

		public String getKorean() {
			return korean;
		}
	}

	public enum ItemStatus {
		WAITING("대기중"), ACTIVE("판매중"), EXPIRED("종료"), RESERVED("예약중"), SOLD("판매완료");

		private final String korean;

		ItemStatus(String korean) {
			this.korean = korean;
		}

		public String getKorean() {
			return korean;
		}
	}

	public enum ItemQuality {
		LOW("하"), MEDIUM("중"), HIGH("상");

		private final String korean;

		ItemQuality(String korean) {
			this.korean = korean;
		}

		public String getKorean() {
			return korean;
		}
	}
}
