package kr.co.anabada.chat.dto;

import java.time.LocalDateTime;

import kr.co.anabada.chat.entity.Chat_Room;
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
    private String buyerNickname;
    
    private Integer sellerUserNo;
    private Integer buyerUserNo;

    
    // Chat_Room 엔티티를 ChatRoomDTO로 변환
    public static ChatRoomDTO fromEntity(Chat_Room chatRoom) {
        return new ChatRoomDTO(
            chatRoom.getRoomNo(),
            chatRoom.getItemTitle(),
            chatRoom.getItemNo(),
            chatRoom.getCreatedAt(),
            chatRoom.getBuyer().getUserNick(),
            chatRoom.getSeller().getUser().getUserNo(),
            chatRoom.getBuyer().getUserNo()

        );
    }
}
