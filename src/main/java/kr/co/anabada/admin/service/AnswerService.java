package kr.co.anabada.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.entity.Answer;
import kr.co.anabada.admin.repository.AnswerRepository;

@Service
public class AnswerService {
	 	@Autowired
	    private AnswerRepository answerRepository;

	    // 답변 저장
	    public void saveAnswer(Answer answer) {
	        answerRepository.save(answer);
	    }
	    
	    public List<Answer> getAnswersByQuestionNo(Integer questionNo) {
	        return answerRepository.findByQuestion_questionNo(questionNo); // questionNo로 답변 목록을 조회
	    }
	    
	    public void deleteAnswer(Answer answer) {
	        answerRepository.delete(answer);
	    }

}
