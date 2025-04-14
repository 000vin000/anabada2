package kr.co.anabada.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.anabada.user.dto.IndividualUserJoinDTO;
import kr.co.anabada.user.service.IndividualUserJoinService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/join/individual")
@Slf4j
public class IndividualUserJoinController {

    @Autowired
    private IndividualUserJoinService userJoinService;

    @PostMapping("/join")
    public ResponseEntity<?> joinUser(@Valid @RequestBody IndividualUserJoinDTO userJoinDTO) {
        try {
            log.info("회원가입 요청 데이터: {}", userJoinDTO);
            userJoinService.joinUser(userJoinDTO);
            log.info("회원가입 완료!");

            return ResponseEntity.ok(Map.of(
                    "message", "회원가입이 완료되었습니다.",
                    "redirectUrl", "/login"
            ));
        } catch (IllegalArgumentException e) {
            log.warn("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    //아이디 중복 체크 API
    @GetMapping("/checkUserId")
    public ResponseEntity<?> checkUserId(@RequestParam String userId) {
        boolean isAvailable = userJoinService.isUserIdAvailable(userId);
        return ResponseEntity.ok(Map.of("available", isAvailable));
    }

    //닉네임 중복 체크 API
    @GetMapping("/checkUserNick")
    public ResponseEntity<?> checkUserNick(@RequestParam String userNick) {
        boolean isAvailable = userJoinService.isUserNickAvailable(userNick);
        return ResponseEntity.ok(Map.of("available", isAvailable));
    }
}