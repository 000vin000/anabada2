package kr.co.anabada.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "item_category")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item_Category {
	@Id
	@Column(name = "categoryNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String categoryNo;
	
	@Column(name = "categoryParentNo", length = 8)
	private String categoryParentNo;
	
	@Column(name = "categoryLevel", nullable = false)
	private Integer categoryLevel;
	
	@Column(name = "categoryName", length = 50, nullable = false)
	private String categoryName;
}
