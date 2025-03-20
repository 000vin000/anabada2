package kr.co.anabada.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.anabada.admin.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	void deleteByQuestion_QuestionNo(Integer questionNo);
	
	 List<Answer> findByQuestion_questionNo(Integer questionNo);
}
