package kr.co.anabada.admin.controller;

import kr.co.anabada.admin.entity.Answer;
import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.service.AnswerService;
import kr.co.anabada.user.repository.QuestionRepository;
import kr.co.anabada.user.repository.UserRepository;
import kr.co.anabada.admin.repository.AdminRepository;
import kr.co.anabada.jwt.JwtTokenHelper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/question")
public class AnswerRestController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    
    @Autowired
    private UserRepository userRepository;

    // 현재 로그인한 관리자 정보 가져오기
    private User getLoggedInUser(HttpServletRequest request) {
        var userTokenInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userTokenInfo == null) {
            return null;
        }
        return userRepository.findById(userTokenInfo.getUserNo()).orElse(null);
    }


    // 답변 작성 처리
    @PostMapping("/answer/{questionNo}")
    public ResponseEntity<?> submitAnswer(@PathVariable Integer questionNo,
                                          @RequestBody Map<String, String> requestData,
                                          HttpServletRequest request) {
        User loggedInUser = getLoggedInUser(request);

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요한 기능입니다."));
        }

        // 질문 조회
        Optional<Question> questionOpt = questionRepository.findById(questionNo);
        if (!questionOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "해당 질문을 찾을 수 없습니다."));
        }

        // 요청에서 answerContent 가져오기
        String answerContent = requestData.get("answerContent");
        if (answerContent == null || answerContent.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "답변 내용을 입력해주세요."));
        }

        // 답변 객체 생성
        Answer answer = new Answer();
        answer.setAnswerContent(answerContent);
        answer.setACreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        answer.setQuestion(questionOpt.get());
        answer.setResponder(loggedInUser);

        // 답변 저장
        answerService.saveAnswer(answer);

        return ResponseEntity.ok(Map.of("message", "답변이 성공적으로 등록되었습니다."));
    }
}
