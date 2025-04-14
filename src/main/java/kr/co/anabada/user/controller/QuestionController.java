package kr.co.anabada.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        return "question/write";
    }

    // 내 문의사항 페이지
    @GetMapping("/mypage/myQuestions")
    public String showUserQuestions() {
        return "mypage/myQuestions";
    }

    // 문의 수정 페이지 이동
    @GetMapping("/edit/{questionNo}")
    public String showEditQuestionForm(@PathVariable Integer questionNo, Model model) {
        Question question = questionRepository.findById(questionNo).orElse(null);
        
        model.addAttribute("question", question);  
        return "question/questionUpdate";  
    }
    
    // 문의 상세 페이지 이동
    @GetMapping("/detail/{questionNo}")
    public String getQuestionDetail(@PathVariable Integer questionNo,
                                    @RequestParam(required = false) Integer index,
                                    Model model) {
        Question question = questionRepository.findByIdWithAnswers(questionNo).orElse(null);
        model.addAttribute("question", question);
        model.addAttribute("index", index); 
        return "mypage/myQuestionDetail";
    }

}
