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

    private boolean isActive;
    
    private String lastMessage;          // 마지막 메시지 내용
    private LocalDateTime lastMessageTime; // 마지막 메시지 전송 시간
    private boolean isRead;              // 읽음 여부 (로그인 유저 기준)
    
    private Integer unreadCount;  // 안읽은 메시지 개수
    
    // Chat_Room 엔티티를 ChatRoomDTO로 변환
    public static ChatRoomDTO fromEntity(Chat_Room room, String lastMessage, LocalDateTime lastMessageTime, int unreadCount) {
        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setRoomNo(room.getRoomNo());
        dto.setItemTitle(room.getItemTitle());
        dto.setItemNo(room.getItemNo());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setBuyerNickname(room.getBuyer().getUserNick());
        dto.setSellerUserNo(room.getSeller().getUser().getUserNo());
        dto.setBuyerUserNo(room.getBuyer().getUserNo());
        dto.setActive(room.isActive());
        dto.setLastMessage(lastMessage);
        dto.setLastMessageTime(lastMessageTime);
        dto.setUnreadCount(unreadCount);
        return dto;
    }
    
    public static ChatRoomDTO fromEntity(Chat_Room chatRoom) {
        return fromEntity(chatRoom, "", null, 0);  
    }



    
    
}
