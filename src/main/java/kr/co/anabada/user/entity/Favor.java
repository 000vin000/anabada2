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
import kr.co.anabada.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Favor")
public class Favor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorNo", nullable = false)
    private Integer favorNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "favorType", nullable = false)
    private FavorType favorType;

    @ManyToOne
    @JoinColumn(name = "itemNo", nullable = false)  
    private Item item;

    @ManyToOne
    @JoinColumn(name = "userNo", nullable = false)
    private User user; 
}

enum FavorType {
    ITEM,
    SELLER
}
