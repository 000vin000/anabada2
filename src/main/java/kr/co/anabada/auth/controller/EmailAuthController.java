package kr.co.anabada.auth.controller;

import jakarta.mail.MessagingException;
import kr.co.anabada.auth.dto.EmailAuthRequestDto;
import kr.co.anabada.auth.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/email")
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    //인증번호 발송 요청
    @PostMapping("/send")
    public ResponseEntity<String> sendCode(@RequestParam("email") String email) {
        try {
            emailAuthService.sendVerificationCode(email);
            return ResponseEntity.ok("인증번호가 이메일로 전송되었습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("메일 전송 중 오류가 발생했습니다.");
        }
    }

    //인증번호 확인 요청
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody EmailAuthRequestDto dto) {
        boolean result = emailAuthService.verifyCode(dto.getEmail(), dto.getCode());

        if (result) {
            return ResponseEntity.ok("이메일 인증에 성공했습니다.");
        } else {
            return ResponseEntity.badRequest().body("인증번호가 올바르지 않거나 만료되었습니다.");
        }
    }
}
