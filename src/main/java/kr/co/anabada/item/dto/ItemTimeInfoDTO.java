package kr.co.anabada.item.dto;

import java.time.LocalDateTime;

import kr.co.anabada.item.entity.Item.ItemStatus;
import lombok.Value;

@Value
public class ItemTimeInfoDTO {
	private LocalDateTime saleStartTime;
	private LocalDateTime saleEndTime;
	private ItemStatus status;
}
