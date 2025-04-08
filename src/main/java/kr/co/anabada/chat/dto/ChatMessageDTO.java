package kr.co.anabada.chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private Integer msgNo;             
    private String msgContent;       
    private LocalDateTime msgDate;     
    private String formattedMsgDate;  
    private Integer senderNo;          // 보낸 사람의 userNo
    private String userNick;           // 보낸 사람 닉네임
    private Integer roomNo;            
    private Boolean msgIsRead;         
    private String messageType;        // 메시지 타입 (NEW or HISTORY)

    // 메시지 내용 반환
    public String getMessage() {
        return this.msgContent;
    }
}
