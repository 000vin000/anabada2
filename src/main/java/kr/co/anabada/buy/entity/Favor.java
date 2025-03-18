package kr.co.anabada.buy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "Favor")
public class Favor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorNo", nullable = false)
    private Integer favorNo;

    @Column(name = "itemNo", nullable = false)
    private Integer itemNo;

    @Column(name = "userNo", nullable = false)
    private Integer userNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "favorType", nullable = false)
    private FavorType favorType;

}

enum FavorType {
    ITEM,
    SELLER
}
