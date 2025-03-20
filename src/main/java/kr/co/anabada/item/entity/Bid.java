package kr.co.anabada.item.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer bidNo;
	@ManyToOne
	@JoinColumn(name = "itemNo", nullable = false)
	private Item item;
	@ManyToOne
	@JoinColumn(name = "userNo", nullable = false)
	private User user;
	@Column(nullable = false, columnDefinition = "ENUM('active','winning','lost','cancelled') default 'active'")
	private String bidStatus;
	@Column(nullable = false)
	private Long bidPrice;
	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime bidTime;
}
// jhu