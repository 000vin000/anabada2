package kr.co.anabada.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatRoomRequest {
	 private Integer sellerId;
	 private Integer buyerId;
	 private String itemTitle;
	 private Integer itemNo;
}
