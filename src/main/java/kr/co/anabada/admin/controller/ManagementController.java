package kr.co.anabada.admin.controller;

import kr.co.anabada.admin.entity.Answer;
import kr.co.anabada.admin.entity.Notice;
import kr.co.anabada.admin.entity.Warn;
import kr.co.anabada.admin.entity.Warn.WarnStatus;
import kr.co.anabada.admin.entity.Withdrawal;
import kr.co.anabada.admin.repository.AnswerRepository;
import kr.co.anabada.admin.repository.NoticeRepository;
import kr.co.anabada.admin.repository.WarnRepository;
import kr.co.anabada.admin.repository.WithdrawalRepository;
import kr.co.anabada.admin.service.WarnService;
import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.repository.QuestionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ManagementController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private WarnService warnService;
    
    @Autowired
    private NoticeRepository noticeRepository;
    
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @GetMapping("/admin/management")
    public String getManagement(Model model) {
    
        List<Question> questions = questionRepository.findAll();
        
        Map<Integer, List<Answer>> answersByQuestionNo = new HashMap<>();

        // 각 질문에 대한 답변 목록 조회
        for (Question question : questions) {
            List<Answer> answers = answerRepository.findByQuestion_questionNo(question.getQuestionNo());
            answersByQuestionNo.put(question.getQuestionNo(), answers);
        }
        
        List<Warn> warns = warnService.findAllByWarnStatus(WarnStatus.REQUESTED);
        
        List<Notice> notices = noticeRepository.findAll();
        
        List<Withdrawal> withdrawals = withdrawalRepository.findAll();


        model.addAttribute("questions", questions);
        model.addAttribute("answersByQuestionNo", answersByQuestionNo);
        model.addAttribute("warns", warns);
        model.addAttribute("notices", notices);
        model.addAttribute("withdrawals", withdrawals);
        
        return "admin/customerManagement";  // 모든 질문을 보여주는 페이지로 이동
    }  
}
