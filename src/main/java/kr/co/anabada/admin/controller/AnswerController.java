package kr.co.anabada.admin.controller;

import kr.co.anabada.admin.entity.Answer;
import kr.co.anabada.admin.service.AnswerService;
import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.repository.QuestionRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/question")
public class AnswerController {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerService answerService;

    // 답변 작성 페이지
    @GetMapping("/answer/{questionNo}")
    public String showAnswerForm(@PathVariable Integer questionNo, Model model) {
        // 해당 questionNo에 해당하는 질문을 가져옴
        Question question = questionRepository.findById(questionNo).orElse(null);
        if (question == null) {
            model.addAttribute("error", "해당 질문을 찾을 수 없습니다.");
            return "error";  // 적절한 에러 페이지로 리디렉션
        }

        model.addAttribute("question", question);
        return "question/answer";  // 답변 작성 페이지로 이동
    }
    
    @PostMapping("/answer/delete/{questionNo}")
    public String deleteAnswer(@PathVariable Integer questionNo, Model model) {
        List<Answer> deletedAnswers = answerService.deleteAnswersByQuestionNo(questionNo);

        if (deletedAnswers.isEmpty()) {
            model.addAttribute("error", "삭제할 답변 정보를 찾을 수 없습니다.");
            return "error";  // 에러 페이지 이동
        }

        model.addAttribute("message", "답변이 성공적으로 삭제되었습니다.");
        model.addAttribute("deletedAnswers", deletedAnswers); // 삭제된 답변 목록 전달
        return "redirect:/admin/management";  // 삭제 후 질문 목록 페이지로 리디렉션
    }

}