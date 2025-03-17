package kr.co.anabada.item.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.item.entity.QnA;
import kr.co.anabada.item.entity.Question;
import kr.co.anabada.item.mapper.QuestionMapper;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    

   
    // 답변할 때 해당하는 특정 질문 
    public Question getQuestionByQNo(int qNo) {
        return questionMapper.getQuestionByQNo(qNo);
    }

    // 마이페이지에서 문의 삭제
    public void deleteQ(int qNo, int itemNo) {
        questionMapper.deleteQ(qNo, itemNo);
    }
    
    // qna페이지에서 문의 삭제
    public void deleteQQ(int qNo, int itemNo, int userNo) {
    	questionMapper.deleteQQ(qNo, itemNo, userNo);
    }

    
    // 내가 작성한 모든 문의글 조회
    public List<QnA> getQList(int userNo) {
        return questionMapper.getQList(userNo); 
    }
    
    // 아이템번호 == 올린사람의넘버 비교
    public Boolean canAnswer(int itemNo, int userNo) {
        Integer itemOwner = questionMapper.getItemOwner(itemNo);
        return itemOwner != null && itemOwner == userNo;
    }
    
    // 질문번호 == 올린사람 넘버 비교
    public Boolean canQDelete (int qNo, int userNo) {
    	Integer qOwner = questionMapper.getQOwner(qNo);
    	return qOwner != null && qOwner == userNo;
    }

   
    // 상품별 문의 목록 조회
    public List<QnA> getQListByItem(int itemNo) {
        return questionMapper.getQListByItem(itemNo);
    }
    
    //상품별 특정 유저 문의 목록 조회
    public List<QnA> getQListByUserForItem(int userNo, int itemNo) {
        return questionMapper.getQListByUserForItem(userNo, itemNo); 
    }

    // 문의 등록
    public void insertQ(int itemNo, int userNo, String qTitle, String qContent, LocalDateTime qDate) {      
        questionMapper.insertQ(itemNo, userNo, qTitle, qContent, qDate);
    }
}