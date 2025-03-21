package kr.co.anabada.chat.entity;

import jakarta.persistence.*;
import kr.co.anabada.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat_Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 채팅방 ID (Integer로 수정)

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller; // 판매자

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer; // 구매자

    @Column(nullable = false)
    private String itemTitle; // 거래 상품 제목

    @Column(nullable = false)
    private Integer itemNo; // 거래 상품 번호 (Integer로 수정)

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일자
}
