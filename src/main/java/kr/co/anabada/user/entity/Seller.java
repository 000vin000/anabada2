package kr.co.anabada.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Seller")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sellerNo", nullable = false)
    private Integer sellerNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "sellerType", nullable = false)
    private SellerType sellerType;

    @Column(name = "sellerDesc", nullable = false)
    private String sellerDesc;

    @Column(name = "sellerItemCnt", nullable = false)
    private Integer sellerItemCnt = 0;

    @Column(name = "sellerTransCnt", nullable = false)
    private Integer sellerTransCnt = 0;

    @Column(name = "sellerTotalSales", nullable = false)
    private Long sellerTotalSales = 0L;

    @Column(name = "sellerGrade", length = 10)
    private String sellerGrade;
    
    @ManyToOne
    @JoinColumn(name = "userNo", nullable = false)
    private User user;



	public enum SellerType {
	    INDIVIDUAL,
	    BRAND
	}
}

