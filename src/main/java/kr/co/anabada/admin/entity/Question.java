package kr.co.anabada.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qNo", nullable = false)
    private Integer qNo;

    @Column(name = "itemNo", nullable = false)
    private Integer itemNo;

    @Column(name = "senderNo", nullable = false)
    private Integer senderNo;

    @Column(name = "receiverNo", nullable = false)
    private Integer receiverNo;

    @Column(name = "qIsPrivate", nullable = false)
    private Boolean qIsPrivate = false;

    @Column(name = "qTitle", nullable = false, length = 255)
    private String qTitle;

    @Column(name = "qContent", nullable = false)
    private String qContent;

    @Column(name = "qDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp qDate;

    
}

