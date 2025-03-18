package kr.co.anabada.buy.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import kr.co.anabada.user.entity.User;
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

    @ManyToOne
    @JoinColumn(name = "bidNo", nullable = false)  
    private Bid bid; 

    @ManyToOne
    @JoinColumn(name = "sellerNo", nullable = false) 
    private User seller; 

    @ManyToOne
    @JoinColumn(name = "buyerNo", nullable = false) 
    private User buyer;  

    @Column(name = "reviewContent", nullable = false, columnDefinition = "TEXT")
    private String reviewContent;  

    @Column(name = "reviewRating", nullable = false)
    private Double reviewRating; 

    @CreationTimestamp
    @Column(name = "reviewCreatedDate", nullable = false, updatable = false)
    private LocalDateTime reviewCreatedDate; 

    @UpdateTimestamp
    @Column(name = "reviewUpdatedDate", nullable = false)
    private LocalDateTime reviewUpdatedDate; 

}
