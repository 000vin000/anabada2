package kr.co.anabada.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.User;
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

    @Column(name = "qIsPrivate", nullable = false)
    private Boolean qIsPrivate = false;

    @Column(name = "qTitle", nullable = false, length = 255)
    private String qTitle;

    @Column(name = "qContent", nullable = false)
    private String qContent;

    @Column(name = "qDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp qDate;

    @ManyToOne
    @JoinColumn(name = "itemNo", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "senderNo", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiverNo", nullable = false)
    private User receiver;
}

