package kr.co.anabada.user.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.anabada.user.dto.UserLoginDTO;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.UserLoginService;
import kr.co.anabada.user.util.JwtUtil;

@RestController
@RequestMapping("/userlogin")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil; // JWT 유틸 주입

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        Optional<User> userOptional = userLoginService.findByUserId(userLoginDTO.getUserId());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "존재하지 않는 아이디입니다."));
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(userLoginDTO.getUserPw(), user.getUserPw())) {
            return ResponseEntity.status(401).body(Map.of("message", "비밀번호가 일치하지 않습니다."));
        }
        
        // ✅ JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getUserId());

        return ResponseEntity.ok(Map.of(
            "message", "환영합니다.",
            "redirectUrl", "/"
        ));
    }
}
