package kr.co.anabada.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "msgIsRead", nullable = false)
    private Boolean msgIsRead = false;

    @Column(name = "msgDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime msgDate;

    @ManyToOne
    @JoinColumn(name = "room_no", referencedColumnName = "id", nullable = false)
    private Chat_Room chatRoom; // 채팅방 설정

    @ManyToOne
    @JoinColumn(name = "sender_no", referencedColumnName = "userNo", nullable = false)
    private User sender; // 발신자 설정
}