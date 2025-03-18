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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemNo;
	
	@Column(nullable = false)
	private Integer sellerNo;
	
	@Column(nullable = false)
	private String categoryNo;
	
	@Column(nullable = false)
	private String itemSaleType;
	
	@Column(length = 50, nullable = false)
	private String itemTitle;
	
	@Column(columnDefinition = "text")
	private String itemContent;
	
	@Column(nullable = false)
	private String itemStatus;
	
	private Integer itemQuality;
	
	@Column(nullable = false)
	private Integer itemQuantity;
	
	private Integer itemPrice;
	
	@Column(nullable = false)
	private Double itemLatitude; // 위도
	
	@Column(nullable = false)
	private Double itemLongitude; // 경도
	
	@CreatedDate
	private LocalDateTime itemCreatedDate;
	
	private LocalDateTime itemStartDate;
	
	private LocalDateTime itemEndDate;
	
	@Column(nullable = false)
	private Integer itemViewCnt; // 조회수
	
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
