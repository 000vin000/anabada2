package kr.co.anabada.user.entity;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.co.anabada.admin.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionNo", nullable = false)
    private Integer questionNo;

    @ManyToOne
    @JoinColumn(name = "senderNo", nullable = false)  
    private User sender; 

    @Column(name = "qIsPrivate", nullable = false)
    private Boolean qIsPrivate = false;

    @Column(name = "questionTitle", nullable = false, length = 255) 
    private String questionTitle;

    @Column(name = "questionContent", nullable = false)
    private String questionContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "questionStatus", nullable = false)
    private QuestionStatus questionStatus = QuestionStatus.WAITING; 

    @Column(name = "qCreatedDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP") 
    private Timestamp qCreatedDate;
    
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Answer> answers;


    public enum QuestionStatus {
        WAITING,
        ANSWERED,
        CLOSED
    }

}
