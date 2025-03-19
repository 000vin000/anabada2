package kr.co.anabada.user.repository;

import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // 사용자별로 질문을 조회하는 메서드
    List<Question> findBySender(User sender); // sender(발신자)가 특정 사용자일 때 질문 리스트 반환
}
