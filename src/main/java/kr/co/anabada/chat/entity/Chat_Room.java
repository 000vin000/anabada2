package kr.co.anabada.chat.entity;

import jakarta.mail.FetchProfile.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Chat_Room")
public class Chat_Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomNo", nullable = false)
    private Integer roomNo;

    @Column(name = "itemNo")
    private Integer itemNo;

    @Column(name = "user1No", nullable = false)
    private Integer user1No;

    @Column(name = "user2No", nullable = false)
    private Integer user2No;

    @Enumerated(EnumType.STRING)
    @Column(name = "roomStatus", nullable = false)
    private RoomStatus roomStatus;

    @Column(name = "roomLastMsgDate")
    private LocalDateTime roomLastMsgDate;

    @Column(name = "roomDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime roomDate;
    
    @ManyToOne
    @JoinColumn(name = "itemNo", nullable = false)
    private Item item;
    
    @OneToOne
    @JoinColumn(name = "user1No", referencedColumnName = "userNo", nullable = false)
    private User user1; 

    @OneToOne
    @JoinColumn(name = "user2No", referencedColumnName = "userNo", nullable = false)
    private User user2; 

}

enum RoomStatus {
    ACTIVE,
    CLOSED
}
