package kr.co.anabada.buy.entity;

import jakarta.persistence.*;
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
@Table(name = "buy", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"itemNo", "userNo"})  
})
public class Buy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "buyNo")
    private Integer buyNo;
    
    @ManyToOne
    @JoinColumn(name = "itemNo", nullable = false)  
    private Item itemNo;  

    @ManyToOne
    @JoinColumn(name = "userNo", nullable = false) 
    private User userNo;  

    @ManyToOne
    @JoinColumn(name = "bidNo", nullable = false) 
    private Bid bidNo; 

}
