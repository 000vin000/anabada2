package kr.co.anabada.chat.dto;

import lombok.Data;
@Data
public class SendMessageDTO {
	private Integer roomNo;
    private Integer senderId;
    private String msgContent; 
}
