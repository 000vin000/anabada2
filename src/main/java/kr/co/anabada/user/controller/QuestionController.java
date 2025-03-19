package kr.co.anabada.user.controller;

import kr.co.anabada.user.service.QuestionService;
import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.QuestionRepository;
import kr.co.anabada.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private UserRepository userRepository;

 // 문의 작성 페이지 이동
    @GetMapping("/write")
    public String showQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        return "question/write";  // 작성 페이지
    }
    
    // 하드코딩된 사용자로 문의 작성 처리
    @PostMapping("/write")
    public String writeQuestion(@ModelAttribute Question question) {
        // 하드코딩된 senderNo 설정
        int hardcodedUserNo = 1;

        // 하드코딩된 User 설정
        User sender = userRepository.findById(hardcodedUserNo).orElse(null);
        if (sender == null) {
            return "redirect:/error";  // 오류 처리 페이지
        }

        // Question 엔티티에 필요한 정보 설정
        question.setSender(sender);
        question.setQCreatedDate(Timestamp.valueOf(LocalDateTime.now()));  // qCreatedDate로 수정

        // Question 저장
        questionService.saveQuestion(question);

        return "redirect:/question/list";  // 문의 목록 페이지로 리다이렉트
    }

    
// 로그인 구현 후 세션 사용
//    // 문의 작성 처리
//    @PostMapping("/write")
//    public String submitQuestion(@ModelAttribute Question question,
//                                 @SessionAttribute(name = "loggedInUser", required = false) User user) {
//        if (user == null) {
//            return "redirect:/login";  
//        }
//
//        question.setSender(user);  
//        questionService.saveQuestion(question);  
//        return "redirect:/question/list";  // 목록 페이지
//    }
}
