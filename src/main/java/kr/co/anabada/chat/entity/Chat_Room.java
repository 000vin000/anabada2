package kr.co.anabada.chat.entity;

import jakarta.persistence.*;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_room")
public class Chat_Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus roomStatus = RoomStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime roomDate;

    @ManyToOne
    @JoinColumn(name = "itemNo", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "sender_no", referencedColumnName = "userNo")
    private User senderNo;

    @ManyToOne
    @JoinColumn(name = "receiver_no", referencedColumnName = "userNo")
    private User receiverNo;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat_Message> messages;

    @PrePersist
    public void prePersist() {
        this.roomDate = LocalDateTime.now();
    }

    public enum RoomStatus {
        ACTIVE,
        CLOSED
    }
}
