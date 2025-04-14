package kr.co.anabada.user.service;

import kr.co.anabada.admin.repository.AnswerRepository;
import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository; 
    
    @Autowired
    private AnswerRepository answerRepository;

    // 문의 저장
    public void saveQuestion(Question question) {
        question.setQCreatedDate(new Timestamp(System.currentTimeMillis()));  // qCreatedDate로 수정
        questionRepository.save(question);
    }

    // 특정 사용자 질문조회
    public List<Question> getQuestionsBySenderNo(Integer senderNo) {
        // senderNo로 관련된 Question을 조회
        List<Question> questions = questionRepository.findBySender_UserNo(senderNo);
        return questions;
    }
    
    public Question getQuestionByNo(Integer questionNo) {
    	Question question = questionRepository.findById(questionNo).orElse(null);
        if (question != null && question.getAnswers() != null) {
            question.getAnswers().size(); // Lazy 초기화 강제
        }
        return question;
    }
    
    @Transactional
    public void deleteQuestionWithAnswers(Integer questionNo) {
        answerRepository.deleteByQuestion_QuestionNo(questionNo);
        questionRepository.deleteById(questionNo);
    }
}
