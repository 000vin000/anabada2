package kr.co.anabada.admin.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.entity.User;
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
    @Column(name = "answerNo", nullable = false)
    private Integer answerNo;

    @Column(name = "answerContent", nullable = false)
    private String answerContent;

    @Column(name = "aCreatedDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp aCreatedDate;
    
    @OneToOne
    @JoinColumn(name = "questionNo", nullable = false)
    private Question question; 
    
    @ManyToOne
    @JoinColumn(name = "responderNo", nullable = false)  
    private User responder;


}

