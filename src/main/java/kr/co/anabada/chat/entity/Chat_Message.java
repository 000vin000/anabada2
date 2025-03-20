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
    @JoinColumn(name = "chat_room_no", referencedColumnName = "roomNo") 
    private Chat_Room chatRoom; 
    
//    @ManyToOne
//    @JoinColumn(name = "sender_no", referencedColumnName = "userNo")
//    private User senderNo; 
//    
//    @ManyToOne
//    @JoinColumn(name = "receiver_no", referencedColumnName = "userNo")
//    private User receiverNo; 

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
