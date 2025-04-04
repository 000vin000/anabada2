package kr.co.anabada.chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
    private Integer roomNo;
    private String itemTitle;
    private Integer itemNo;
    private LocalDateTime createdAt;
}
