package kr.co.anabada.chat.dto;

import kr.co.anabada.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {
    private Integer roomId;
    private String msgContent;
    private User sender;  // 실제 사용자 정보를 받을 수 있도록 설정
}

