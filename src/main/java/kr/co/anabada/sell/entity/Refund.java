package kr.co.anabada.sell.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import kr.co.anabada.buy.entity.Payment;
import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.entity.Item;
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
    
    @ManyToOne
    @JoinColumn(name = "payNo", nullable = false)
    private Payment payment; 

    @Column(name = "refundReason", columnDefinition = "TEXT", nullable = false)
    private String refundReason; 

    @Enumerated(EnumType.STRING)
    @Column(name = "refundStatus", nullable = false)
    private RefoundStatus refundStatus;  

    @CreationTimestamp
    @Column(name = "refundDate", nullable = false, updatable = false)
    private LocalDateTime refundDate;  

    @UpdateTimestamp
    @Column(name = "refundProcessedDate")
    private LocalDateTime refundProcessedDate;  
    
    
    public enum RefoundStatus{
    	REQUESTED,
    	REJECTED,
    	COMPLETED
    }

}

