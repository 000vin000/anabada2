package kr.co.anabada.buy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewNo")
    private Integer reviewNo;

    @JoinColumn(name = "bidNo", nullable = false)  
    private Bid bid; 

    @JoinColumn(name = "sellerNo", nullable = false) 
    private User seller; 

    @JoinColumn(name = "buyerNo", nullable = false) 
    private User buyer;  

    @Column(name = "reviewContent", nullable = false, columnDefinition = "TEXT")
    private String reviewContent;  

    @Column(name = "reviewRating", nullable = false)
    private Double reviewRating; 

    @Column(name = "reviewCreatedDate", nullable = false, updatable = false)
    private LocalDateTime reviewCreatedDate = LocalDateTime.now(); 

    @Column(name = "reviewUpdatedDate", nullable = false)
    private LocalDateTime reviewUpdatedDate = LocalDateTime.now(); 

    @PrePersist
    @PreUpdate
    public void validateReviewRating() {
        if (reviewRating < 1.0 || reviewRating > 5.0) {
            throw new IllegalArgumentException("평점은 1.0 이상 5.0 이하여야 합니다.");
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.reviewUpdatedDate = LocalDateTime.now(); 
    }
}
