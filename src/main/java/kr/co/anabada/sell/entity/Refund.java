package kr.co.anabada.sell.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refund")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "refundNo")
    private Integer refundNo;

    @ManyToOne
    @JoinColumn(name = "itemNo", nullable = false) 
    private Item item;  

    @ManyToOne
    @JoinColumn(name = "userNo", nullable = false)  
    private User user; 

    @Column(name = "refundReason", columnDefinition = "TEXT", nullable = false)
    private String refundReason; 

    @Column(name = "refundStatus", nullable = false)
    private String refundStatus = "requested";  

    @Column(name = "refundDate", nullable = false, updatable = false)
    private LocalDateTime refundDate = LocalDateTime.now();  

    @Column(name = "refundProcessedDate")
    private LocalDateTime refundProcessedDate;  

  
    @PrePersist
    public void prePersist() {
        if (refundStatus == null) {
            refundStatus = "requested";  
        }
        if (refundDate == null) {
            refundDate = LocalDateTime.now(); 
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (refundProcessedDate == null && "completed".equals(refundStatus)) {
            refundProcessedDate = LocalDateTime.now(); 
        }
    }
}

