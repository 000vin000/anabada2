package kr.co.anabada.admin.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "Answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aNo", nullable = false)
    private Integer aNo;

    @Column(name = "qNo", nullable = false)
    private Integer qNo;

    @Column(name = "responderNo", nullable = false)
    private Integer responderNo;

    @Column(name = "aContent", nullable = false)
    private String aContent;

    @Column(name = "aDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp aDate;

}

