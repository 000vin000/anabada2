package kr.co.anabada.user.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.anabada.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavorItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorNo", nullable = false)
    private Integer favorNo;
    @ManyToOne
    @JoinColumn(name = "itemNo", nullable = false)  
    private Item item;
    @ManyToOne
    @JoinColumn(name = "userNo", nullable = false)
    private User user; 
    @CreatedDate
    private LocalDateTime favorCreatedDate;
} // jhu