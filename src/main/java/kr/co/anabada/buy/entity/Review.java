package kr.co.anabada.buy.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewNo")
    private Integer reviewNo;

    @OneToOne
    @JoinColumn(name = "itemNo", nullable = false)  
    private Item item; // 아이템 기준 리뷰 조회 복잡성 낮추기

    @OneToOne
    @JoinColumn(name = "sellerNo", nullable = false) 
    private Seller seller; 

    @OneToOne
    @JoinColumn(name = "buyerNo", nullable = false) 
    private Buyer buyer;  

    @Column(name = "reviewContent", nullable = false, columnDefinition = "TEXT")
    private String reviewContent;

    @Column(name = "reviewRating", nullable = false)
    private Double reviewRating;

    @CreationTimestamp
    @Column(name = "reviewCreatedDate", nullable = false, updatable = false)
    private LocalDateTime reviewCreatedDate;
}
