package kr.co.anabada.coin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.co.anabada.coin.entity.ChangeCoin;
import kr.co.anabada.item.entity.Item;
import lombok.Data;

@Data
public class ChangeCoinDTO {
    private Integer changecoinNo;
    private String changecoinTypeKorean;
    private BigDecimal changecoinAmount;
    private LocalDateTime changecoinAt;
    private Item itemNo;
    
    // 생성자, getter, setter
    public ChangeCoinDTO(ChangeCoin changeCoin) {
        this.changecoinNo = changeCoin.getChangecoinNo();
        this.changecoinTypeKorean = changeCoin.getChangecoinType().getKorean(); 
        this.changecoinAmount = changeCoin.getChangecoinAmount();
        this.changecoinAt = changeCoin.getChangecoinAt();
        this.itemNo = changeCoin.getItemNo();
    }
}
