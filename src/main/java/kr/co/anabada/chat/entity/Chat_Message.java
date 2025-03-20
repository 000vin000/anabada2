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
@Table(name = "Chat_Message")
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

    // 채팅방과 다대일 관계
    @ManyToOne
    @JoinColumn(name = "roomNo", nullable = false)
    private Chat_Room chatRoom;

    // 발신자와 다대일 관계
    @ManyToOne
    @JoinColumn(name = "senderNo", nullable = false)
    private User sender;
}