package kr.co.anabada.item.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnA {
   private int qNo;
   private int itemNo;
   private int userNo;
   private String itemName;
   private String userNick;
   private String qTitle;
   private String qContent;
   private LocalDateTime qDate;       
   private String aContent; 
   private LocalDateTime aDate;

    //LocalDateTimr을 자바스크립트에서 바로 사용할 수 없어서 바꿔주는 메서드 생성함
    public String getFormattedQDate(LocalDateTime qDate) {
        return qDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public String getFormattedADate(LocalDateTime aDate) {
       if (aDate == null) {
          return null;
       }
        return aDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
