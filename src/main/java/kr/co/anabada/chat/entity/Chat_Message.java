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
    private Integer msgNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomNo", nullable = false) 
    private Chat_Room chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderNo", nullable = false)  
    private User sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverNo", nullable = false)  
    private User receiver;

    @Column(nullable = false)
    private String senderNick;

    @Column(nullable = false)
    private String receiverNick;

    @Column(nullable = false)
    private String msgContent;

    @Column(nullable = false)
    private LocalDateTime msgDate;

    @Column(nullable = false)
    private boolean msgIsRead;
}
