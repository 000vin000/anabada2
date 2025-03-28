package kr.co.anabada.chat.dto;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class ChatMessageDTO {
    private Integer msgNo;
    private String msgContent;
    private Boolean msgIsRead;
    private LocalDateTime msgDate;
    private String formattedMsgDate;
    private SenderDTO sender;
    private ChatRoomDTO chatRoom;
    private Integer roomNo;
}
