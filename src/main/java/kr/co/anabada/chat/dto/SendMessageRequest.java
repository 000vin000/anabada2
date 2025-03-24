package kr.co.anabada.chat.dto;

import lombok.Data;
@Data
public class SendMessageRequest {
    private Integer roomNo;   // 채팅방 번호
    private String msgContent;  // 메시지 내용
    private Integer senderId;  // 발신자 ID (User ID)
}
