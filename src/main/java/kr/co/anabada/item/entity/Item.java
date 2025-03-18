package kr.co.anabada.item.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
	@Column(name = "itemNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemNo;
	
	@Column(name = "sellerNo", nullable = false)
	private Integer sellerNo;
	
	@Column(name = "categoryNo", nullable = false)
	private String categoryNo;
	
	@Column(name = "itemSaleType", nullable = false)
	private String itemSaleType;
	
	@Column(name = "itemTitle", length = 50, nullable = false)
	private String itemTitle;
	
	@Column(name = "itemContent", columnDefinition = "text")
	private String itemContent;
	
	@Column(name = "itemStatus", nullable = false)
	private String itemStatus;
	
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
	
	@CreatedDate
	@Column(name = "itemCreateDate")
	private LocalDateTime itemCreatedDate;
	
	@Column(name = "itemStartDate")
	private LocalDateTime itemStartDate;
	
	@Column(name = "itemEndDate")
	private LocalDateTime itemEndDate;
	
	@Column(name = "itemViewCnt", nullable = false)
	private Integer itemViewCnt; // 조회수
	
	@Column(name = "itemAvgRating")
	private Double itemAvgRating; // 평균 평점
	
	// 기본값 설정
	@PrePersist
	public void prePersist() {
		if (itemStatus == null) { itemStatus = "active"; }
		if (itemQuantity == null) { itemQuality = 1; }
		if (itemPrice == null) { itemPrice = 0; }
		if (itemViewCnt == null) { itemViewCnt = 0; }
		if (itemStartDate == null) { itemStartDate = itemCreatedDate; }
		if (itemEndDate == null) { itemEndDate = itemStartDate.plusDays(3); }
	}
}
