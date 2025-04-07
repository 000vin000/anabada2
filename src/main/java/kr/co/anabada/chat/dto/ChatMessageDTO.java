package kr.co.anabada.chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private Integer msgNo;
    private String msgContent;
    private LocalDateTime msgDate;
    private String formattedMsgDate;
    private Integer senderNo; 
    private Integer roomNo;
    
    // msgContent 반환
    public String getMessage() {
        return this.msgContent;
    }
}
