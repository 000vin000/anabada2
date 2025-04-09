package kr.co.anabada.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Buyer")
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buyerNo", nullable = false)
    private Integer buyerNo;
    
    @ManyToOne
    @JoinColumn(name = "userNo", nullable = false)
    private User user; 

    @Column(name = "buyerBidCnt", nullable = false)
    @Builder.Default
    private Integer buyerBidCnt = 0;

    @Column(name = "buyerSuccessBidCnt", nullable = false)
    @Builder.Default
    private Integer buyerSuccessBidCnt = 0;

    @Column(name = "buyerPayCnt", nullable = false)
    @Builder.Default
    private Integer buyerPayCnt = 0;

    @Column(name = "buyerBidSuccessRate", nullable = false)
    @Builder.Default
    private Double buyerBidSuccessRate = 0.0;

    @Column(name = "buyerPaySuccessRate", nullable = false)
    @Builder.Default
    private Double buyerPaySuccessRate = 0.0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime buyerCreatedDate;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime buyerUpdatedDate;
}
