package kr.co.anabada.chat.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatRoomDTO {
    private Integer roomNo;
    private String itemTitle;
    private Integer itemNo;
    private LocalDateTime createdAt;
}
