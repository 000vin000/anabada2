package kr.co.anabada.item.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.item.entity.Answer;
import kr.co.anabada.item.entity.QnA;
import kr.co.anabada.item.mapper.AnswerMapper;

@Service
public class AnswerService {
	
	@Autowired
	private AnswerMapper answerMapper;
	
    // 내가 받은 모든 문의글 조회
    public List<QnA> getAList(int userNo) {
        return answerMapper.getAList(userNo); 
    }
    
    // 답변 등록
    public void insertA(int qNo, int userNo, String aContent, LocalDateTime aDate) {
    	answerMapper.insertA(qNo, userNo, aContent, aDate);
    }
}
