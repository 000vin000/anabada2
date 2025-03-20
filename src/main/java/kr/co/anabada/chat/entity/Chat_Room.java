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
@Table(name = "Chat_Room")
public class Chat_Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomNo", nullable = false)
    private Integer roomNo;

    @Column(name = "itemNo", insertable = false, updatable = false)
    private Integer itemNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "roomStatus", nullable = false)
    private RoomStatus roomStatus;

    @Column(name = "roomLastMsgDate")
    private LocalDateTime roomLastMsgDate;

    @Column(name = "roomDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime roomDate;

    @ManyToOne
    @JoinColumn(name = "itemNo", nullable = false, insertable = false, updatable = false)
    private Item item;

    // 채팅 참여자
    @ManyToOne
    @JoinColumn(name = "user1No", referencedColumnName = "userNo", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2No", referencedColumnName = "userNo", nullable = false)
    private User user2;

    // 채팅방의 메시지들 (양방향 연관 관계)
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat_Message> messages;
}

enum RoomStatus {
    ACTIVE,
    CLOSED
}