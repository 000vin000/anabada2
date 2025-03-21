package kr.co.anabada.chat.entity;

import jakarta.persistence.*;
import kr.co.anabada.item.entity.Item;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "roomStatus", nullable = false)
    private RoomStatus roomStatus = RoomStatus.ACTIVE;

    @Column(name = "roomDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime roomDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "itemNo", nullable = false)
    private Item item;

    // 구매자 (채팅을 시작하는 사용자)
    @ManyToOne
    @JoinColumn(name = "buyerNo", referencedColumnName = "userNo", nullable = false)
    private User buyer;

    // 판매자 (해당 아이템의 판매자)
    @ManyToOne
    @JoinColumn(name = "sellerNo", referencedColumnName = "userNo", nullable = false)
    private User seller;
}

enum RoomStatus {
    ACTIVE,
    CLOSED
}
