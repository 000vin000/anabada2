package kr.co.anabada.coin.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "goods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods { // 유저 현재 보유 재화

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer goodsNo;

    @OneToOne
    @JoinColumn(name = "userNo", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal goodsCash;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal goodsCoin;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime goodsCreatedAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime goodsAt;

    // 새로운 엔티티가 생성될 때 기본값 설정
    @PrePersist
    public void prePersist() {
        if (goodsCash == null) {
            this.goodsCash = BigDecimal.ZERO;
        }
        if (goodsCoin == null) {
            this.goodsCoin = BigDecimal.ZERO;
        }
        this.goodsAt = LocalDateTime.now();
    }

    // 📌 업데이트될 때 최신 시간 자동 설정
    @PreUpdate
    public void preUpdate() {
        this.goodsAt = LocalDateTime.now();
    }
}
