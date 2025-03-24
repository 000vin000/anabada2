package kr.co.anabada.chat.entity;

import jakarta.persistence.*;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_message")  
public class Chat_Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msgNo", nullable = false)
    private Integer msgNo;

    @Column(name = "msgContent", nullable = false, columnDefinition = "TEXT")
    private String msgContent;

    @Column(name = "msgIsRead", nullable = false)
    private Boolean msgIsRead = false;

    @Column(name = "msgDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime msgDate;

    @ManyToOne  // 변경: @OneToOne -> @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)  // roomNo -> chat_room_id
    private Chat_Room chatRoom; // 채팅방 정보 (다대일 관계로 변경)

    @ManyToOne  // 변경: @OneToOne -> @ManyToOne
    @JoinColumn(name = "sender_no", referencedColumnName = "userNo", nullable = false)  // senderNo -> sender_no
    private User sender;  // 메시지 발송자 (다대일 관계)
}
