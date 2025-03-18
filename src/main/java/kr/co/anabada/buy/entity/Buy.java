package kr.co.anabada.buy.entity;

import jakarta.persistence.*;
import kr.co.anabada.item.entity.Item; 
import kr.co.anabada.user.entity.User;  
import kr.co.anabada.buy.entity.Bid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "buy", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"itemNo", "userNo"})  
})
public class Buy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "buyNo")
    private Integer buyNo;  

    @JoinColumn(name = "itemNo", nullable = false)  
    private Item item;  

    @JoinColumn(name = "userNo", nullable = false) 
    private User user;  

    @JoinColumn(name = "bidNo", nullable = false) 
    private Bid bid; 

}
