package kr.co.anabada.buy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import kr.co.anabada.item.entity.Item;
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
	@JoinColumn(name = "fk_itemNo")
	@NotNull
	private Item item;
	@ManyToOne
	@JoinColumn(name = "fk_userNo")
	@NotNull
	private User user;
	@NotNull
	private Integer bidPrice;
	@NotNull
	private LocalDateTime bidTime;
}
// jhu