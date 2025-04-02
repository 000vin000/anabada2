package kr.co.anabada.chat.entity;

import jakarta.persistence.*;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Chat_Message")
public class Chat_Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msgNo", nullable = false)
    private Integer msgNo;

    @Column(name = "msgContent", nullable = false, columnDefinition = "TEXT")
    private String msgContent;

    @Column(name = "msgDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime msgDate;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩. 필요할 때만 로딩 됨
    @JoinColumn(name = "room_no", nullable = false)
    private Chat_Room chatRoom; // 채팅방 설정

    @ManyToOne
    @JoinColumn(name = "sender_no", referencedColumnName = "userNo", nullable = false)
    private User sender; // 발신자 설정
    
    @Transient
    private String formattedMsgDate;

    @PostLoad
    public void formatMsgDate() {
        this.formattedMsgDate = msgDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public Integer getRoomNo() {
        return this.chatRoom.getRoomNo();
    }
}
