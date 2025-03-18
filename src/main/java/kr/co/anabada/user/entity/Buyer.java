package kr.co.anabada.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Buyer")
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buyerNo", nullable = false)
    private Integer buyerNo;

    @Column(name = "userNo", nullable = false, unique = true)
    private Integer userNo;

    @Column(name = "buyerBidCnt", nullable = false)
    private Integer buyerBidCnt = 0;

    @Column(name = "buyerSuccessBidCnt", nullable = false)
    private Integer buyerSuccessBidCnt = 0;

    @Column(name = "buyerPayCnt", nullable = false)
    private Integer buyerPayCnt = 0;

    @Column(name = "buyerBidSuccessRate", nullable = false)
    private Double buyerBidSuccessRate = 0.0;

    @Column(name = "buyerPaySuccessRate", nullable = false)
    private Double buyerPaySuccessRate = 0.0;


}
