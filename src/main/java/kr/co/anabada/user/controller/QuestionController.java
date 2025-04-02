package kr.co.anabada.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.repository.QuestionRepository;
import kr.co.anabada.user.service.QuestionService;


@Controller
@RequestMapping("/question")
public class QuestionController {
	
	 @Autowired
	 private QuestionRepository questionRepository;


    // 문의 작성 페이지 이동
    @GetMapping("/write")
    public String showQuestionForm() {
        return "question/write"; // "question/write" JSP 페이지로 이동
    }

    // 내 문의사항 페이지
    @GetMapping("/mypage/myQuestions")
    public String showUserQuestions() {
        return "mypage/myQuestions"; // "mypage/myQuestions" JSP 페이지로 이동
    }

    // 문의 수정 페이지 이동
    @GetMapping("/edit/{questionNo}")
    public String showEditQuestionForm(@PathVariable Integer questionNo, Model model) {
        Question question = questionRepository.findById(questionNo).orElse(null);
        
        model.addAttribute("question", question);  // 수정할 문의사항 데이터를 JSP로 전달
        return "question/questionUpdate";  // 수정 페이지로 이동
    }
}
