package kr.co.anabada.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "image")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
	@Id
	@Column(name = "imageNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageNo;
	
	@ManyToOne
	@JoinColumn(name = "itemNo", nullable = false)
	private Item itemNo;
	
	@Column(name = "imageFile", nullable = false)
	@Lob
	private byte[] imageFile;
}
