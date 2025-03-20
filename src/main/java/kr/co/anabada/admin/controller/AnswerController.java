package kr.co.anabada.admin.controller;

import kr.co.anabada.admin.entity.Answer;
import kr.co.anabada.user.entity.Question;
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.service.AnswerService;
import kr.co.anabada.user.repository.QuestionRepository;
import kr.co.anabada.admin.repository.AdminRepository;
import kr.co.anabada.admin.repository.AnswerRepository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/question")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AdminRepository adminRepository; 
    
    @Autowired
    private AnswerRepository answerRepository;

    // 답변 작성 페이지
    @GetMapping("/answer/{questionNo}")
    public String showAnswerForm(@PathVariable Integer questionNo, Model model) {
        // 해당 questionNo에 해당하는 질문을 가져옴
        Question question = questionRepository.findById(questionNo).orElse(null);
        if (question == null) {
            // 질문이 없으면 에러 페이지로 리디렉션
            model.addAttribute("error", "해당 질문을 찾을 수 없습니다.");
            return "error";  // 적절한 에러 페이지로 리디렉션
        }

        model.addAttribute("question", question);
        return "question/answer";  // 답변 작성 페이지로 이동
    }

    // 답변 작성 처리
    @PostMapping("/answer/{questionNo}")
    public String submitAnswer(@PathVariable Integer questionNo,
                               @RequestParam String answerContent) {
        // 하드코딩된 관리자 번호
        int hardcodedAdminNo = 1;

        // 관리자 정보를 하드코딩된 번호로 조회
        Admin responder = adminRepository.findById(hardcodedAdminNo).orElse(null);
        if (responder == null) {
            return "redirect:/error";  // 관리자 정보가 없으면 에러 페이지로 리디렉션
        }

        // 해당 질문 조회
        Question question = questionRepository.findById(questionNo).orElse(null);
        if (question == null) {
            return "redirect:/question/";  // 질문이 없으면 목록 페이지로 리디렉션
        }

        // 답변 객체 생성 및 세팅
        Answer answer = new Answer();
        answer.setAnswerContent(answerContent);
        answer.setACreatedDate(new Timestamp(System.currentTimeMillis()));
        answer.setQuestion(question); // 질문 설정
        answer.setResponder(responder);  // 하드코딩된 관리자 설정

        // 답변 저장
        answerService.saveAnswer(answer);

        return "redirect:/question/answerList";  // 답변을 완료한 후, 해당 질문 보기 페이지로 리디렉션
    }
    
    @GetMapping("/answerList")
    public String viewAllQuestions(Model model) {
    	
        List<Question> questions = questionRepository.findAll();
        List<Answer> answers = answerRepository.findAll();

        model.addAttribute("questions", questions);
        model.addAttribute("answers", answers);
        return "question/answerList";  // 모든 질문을 보여주는 페이지로 이동
    }
    
    @PostMapping("/deleteAnswer/{answerNo}")
    public String deleteAnswer(@PathVariable Integer answerNo) {
        // 해당 답변을 삭제
        Answer answer = answerRepository.findById(answerNo).orElse(null);

        return "redirect:/question/answerList";  // 삭제 후 답변 목록 페이지로 리디렉션
    }

}
