package kr.co.anabada.user.controller;

import kr.co.anabada.user.service.QuestionService;
import kr.co.anabada.admin.repository.AnswerRepository;
import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.QuestionRepository;
import kr.co.anabada.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AnswerRepository answerRepository;

    // 하드코딩된 사용자 ID (예시로 사용자 ID = 1을 사용)
    private static final int HARD_CODED_USER_ID = 1; 

    // 문의 작성 페이지 이동
    @GetMapping("/write")
    public String showQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        return "question/write";
    }
    
    // 하드코딩된 사용자로 문의 작성 처리
    @PostMapping("/write")
    public String writeQuestion(@ModelAttribute Question question) {
        // 하드코딩된 사용자 ID로 사용자 조회
        User loggedInUser = userRepository.findById(HARD_CODED_USER_ID).orElse(null);

        if (loggedInUser == null) {
            return "redirect:/error";  // 오류 페이지로 리다이렉트
        }

        // Question 엔티티에 필요한 정보 설정
        question.setSender(loggedInUser);
        question.setQCreatedDate(Timestamp.valueOf(LocalDateTime.now()));  // qCreatedDate로 수정

        // Question 저장
        questionService.saveQuestion(question);

        return "redirect:/question/mypage/myQuestions";  // 문의 목록 페이지로 리다이렉트
    }

    // 내 문의사항 페이지 
    @GetMapping("/mypage/myQuestions")
    public String showUserQuestions(Model model) {
        // 하드코딩된 사용자 ID로 사용자 조회
        User loggedInUser = userRepository.findById(HARD_CODED_USER_ID).orElse(null);

        if (loggedInUser == null) {
            return "redirect:/error"; // 오류 페이지로 리디렉션
        }

        // 하드코딩된 사용자 ID로 문의 목록 조회
        List<Question> questions = questionRepository.findBySender_UserNo(loggedInUser.getUserNo());
        model.addAttribute("questions", questions);

        return "mypage/myQuestions"; // mypage 폴더에 있는 JSP 페이지로 이동
    }

    // 문의 삭제 처리 
    @Transactional
    @PostMapping("/delete/{questionNo}")
    public String deleteQuestion(@PathVariable Integer questionNo, @RequestParam(required = false) String from) {
        // 문의사항 삭제
        answerRepository.deleteByQuestion_QuestionNo(questionNo);
        questionRepository.deleteById(questionNo);

        if (from != null && from.equals("mypage")) {
            return "redirect:/question/mypage/myQuestions";  
        } else {
            return "redirect:/management";  
        }
    }

    // 문의 수정 페이지 이동
    @GetMapping("/edit/{questionNo}")
    public String showEditQuestionForm(@PathVariable Integer questionNo, Model model) {
        Question question = questionRepository.findById(questionNo).orElse(null);
        if (question == null) {
            return "redirect:/error";
        }

        model.addAttribute("question", question);
        return "question/questionUpdate"; // 수정 페이지로 이동
    }

    // 문의 수정 처리
    @PostMapping("/edit/{questionNo}")
    public String updateQuestion(@PathVariable Integer questionNo, @ModelAttribute Question updatedQuestion) {
        Question question = questionRepository.findById(questionNo).orElse(null);
        if (question == null) {
            return "redirect:/error";
        }

        // 기존 문의사항 정보 업데이트
        question.setQuestionTitle(updatedQuestion.getQuestionTitle());
        question.setQuestionContent(updatedQuestion.getQuestionContent());

        // 수정된 내용 저장
        questionRepository.save(question);

        return "redirect:/question/mypage/myQuestions"; 
    }
}
