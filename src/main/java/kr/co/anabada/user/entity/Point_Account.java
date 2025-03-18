package kr.co.anabada.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "point_account", uniqueConstraints = {
    @UniqueConstraint(columnNames = "buyerNo")
})
public class Point_Account {

    @Id
    @Column(name = "buyerNo", nullable = false)
    private Integer buyerNo;

    @OneToOne
    @JoinColumn(name = "buyerNo", insertable = false, updatable = false)
    private User user; 

    @Column(name = "pointBalance", nullable = false, precision = 12, scale = 2)
    private BigDecimal pointBalance = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "pointCreatedDate", nullable = false, updatable = false)
    private LocalDateTime pointCreatedDate; 

    @Column(name = "pointUpdatedDate", nullable = false)
    @UpdateTimestamp
    private LocalDateTime pointUpdatedDate;

}

