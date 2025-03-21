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
    private LocalDateTime msgDate = LocalDateTime.now();

    // 한 채팅방에 여러 개의 메시지가 포함될 수 있음
    @ManyToOne
    @JoinColumn(name = "roomNo", nullable = false)
    private Chat_Room chatRoom;

    // 메시지를 보낸 사람 (구매자 또는 판매자)
    @ManyToOne
    @JoinColumn(name = "senderNo", referencedColumnName = "userNo", nullable = false)
    private User sender;
}
