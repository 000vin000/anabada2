package kr.co.anabada.user.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.anabada.user.dto.UserJoinDTO;
import kr.co.anabada.user.service.UserJoinService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/userjoin")
@Slf4j
public class UserJoinController {

    @Autowired
    private UserJoinService userJoinService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserJoinDTO userJoinDTO) {
        log.info("회원가입 요청 데이터: {}", userJoinDTO);
        userJoinService.registerUser(userJoinDTO);
        log.info("회원가입 완료!");
        
        return ResponseEntity.ok(Map.of(
            "message", "회원가입 성공!",
            "redirectUrl", "/auth/login.html"
        ));
    }
}