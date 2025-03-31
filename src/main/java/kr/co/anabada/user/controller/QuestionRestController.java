package kr.co.anabada.user.controller;

import kr.co.anabada.user.entity.Question;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.QuestionService;
import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.user.repository.QuestionRepository;
import kr.co.anabada.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/question")
public class QuestionRestController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    // 현재 로그인한 사용자 정보 가져오기
    private User getLoggedInUser(HttpServletRequest request) {
        var userTokenInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userTokenInfo == null) {
            return null;
        }
        return userRepository.findById(userTokenInfo.getUserNo()).orElse(null);
    }

    // 문의 작성 처리
    @PostMapping("/write")
    public ResponseEntity<?> writeQuestion(@RequestBody Question question, HttpServletRequest request) {
        User loggedInUser = getLoggedInUser(request);

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요한 기능입니다"));
        }

        question.setSender(loggedInUser);
        question.setQCreatedDate(Timestamp.valueOf(LocalDateTime.now()));

        // Question 저장
        questionService.saveQuestion(question);

        return ResponseEntity.ok(Map.of("message", "문의가 성공적으로 등록되었습니다."));
    }

    // 내 문의사항 목록 조회
    @GetMapping("/mypage/myQuestions")
    public ResponseEntity<?> showUserQuestions(HttpServletRequest request) {
        User loggedInUser = getLoggedInUser(request);

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요한 기능입니다"));
        }

        List<Question> questions = questionRepository.findBySender_UserNo(loggedInUser.getUserNo());

        return ResponseEntity.ok(questions);
    }

    // 문의 삭제 처리
    @DeleteMapping("/delete/{questionNo}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Integer questionNo, HttpServletRequest request) {
        // 로그인한 사용자 가져오기
        User loggedInUser = getLoggedInUser(request);

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요한 기능입니다"));
        }

        // questionNo에 해당하는 문의사항을 조회
        Question question = questionRepository.findById(questionNo).orElse(null);
        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "문의가 존재하지 않습니다"));
        }

        // 삭제 작업 수행
        questionRepository.delete(question);
        return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
    }


    // 문의 수정 처리 (PUT 방식)
    @PutMapping("/edit/{questionNo}")
    public ResponseEntity<?> updateQuestion(@PathVariable Integer questionNo, @RequestBody Question updatedQuestion, HttpServletRequest request) {
        // 로그인된 사용자 가져오기
        User loggedInUser = getLoggedInUser(request);

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요한 기능입니다"));
        }

        // questionNo에 해당하는 문의사항을 조회
        Question question = questionRepository.findById(questionNo).orElse(null);
        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "문의가 존재하지 않습니다"));
        }
        

        // 문의 제목과 내용을 수정
        question.setQuestionTitle(updatedQuestion.getQuestionTitle());
        question.setQuestionContent(updatedQuestion.getQuestionContent());

        // 수정된 질문 저장
        questionRepository.save(question);

        return ResponseEntity.ok(Map.of("message", "문의가 성공적으로 수정되었습니다."));
    }


}
