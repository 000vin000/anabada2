package kr.co.anabada.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequestDto {
    private Integer sellerUserNo;
    private Integer buyerUserNo;
    private Integer itemNo;
}

