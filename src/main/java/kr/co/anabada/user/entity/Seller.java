package kr.co.anabada.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    
    @ManyToOne
    @JoinColumn(name = "userNo", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "sellerType", nullable = false)
    private SellerType sellerType;

    @Column(name = "sellerDesc", nullable = false)
    private String sellerDesc;

    @Column(name = "sellerItemCnt", nullable = false)
    private int sellerItemCnt = 0;

    @Column(name = "sellerTransCnt", nullable = false)
    private int sellerTransCnt = 0;

	@Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal sellerTotalSales = BigDecimal.ZERO;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SellerGrade sellerGrade = SellerGrade.HANGER;
    
    @Column(name="sellerAvgRating", nullable = false)
    private double sellerAvgRating = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime sellerCreatedDate;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime sellerUpdatedDate;



	public enum SellerType {
	    INDIVIDUAL,
	    BRAND
	}
	
	public enum SellerGrade {
	    HANGER(0, 1, "옷걸이"),
	    BUNDLE(2, 3, "보따리"),
	    SMALL_SHOP(4, 50, "구멍가게"),
	    STORE(51, 100, "상점"),
	    MART(101, Integer.MAX_VALUE, "마트");
	    
	    private final int minSalesCount;
	    private final int maxSalesCount;
	    private final String korean;
	    
	    SellerGrade(int minSalesCount, int maxSalesCount, String korean) {
	        this.minSalesCount = minSalesCount;
	        this.maxSalesCount = maxSalesCount;
	        this.korean = korean;
	    }
	    
	    public static SellerGrade fromSalesCount(int salesCount) {
	        SellerGrade[] grades = values(); 
	        Arrays.sort(grades, Comparator.comparing(SellerGrade::getMaxSales).reversed());
	        
	        for (SellerGrade grade : grades) {
	            if (salesCount >= grade.minSalesCount && salesCount <= grade.maxSalesCount) {
	                return grade;
	            }
	        }
	        return HANGER;
	    }
	    
	    public int getMinSales() { return minSalesCount; }
	    public int getMaxSales() { return maxSalesCount; }
	    public String getKorean() { return korean; }
	}
}

