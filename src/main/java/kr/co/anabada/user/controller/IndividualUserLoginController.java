package kr.co.anabada.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.jwt.JwtUtil;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.IndividualUserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/login/individual")
@Slf4j
public class IndividualUserLoginController {

    private final JwtUtil jwtUtil;
    private final IndividualUserLoginService userLoginService;
    private final BCryptPasswordEncoder passwordEncoder;

    public IndividualUserLoginController(JwtUtil jwtUtil, IndividualUserLoginService userLoginService, BCryptPasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userLoginService = userLoginService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestData) {
        String userId = requestData.get("userId");
        String userPw = requestData.get("userPw");

        log.info("로그인 요청: userId={}, userPw={}", userId, userPw);

        Optional<User> userOpt = userLoginService.findByUserId(userId);
        if (userOpt.isEmpty()) {
            log.warn("존재하지 않는 사용자: {}", userId);
            return ResponseEntity.status(401).body(Map.of("message", "존재하지 않는 사용자입니다."));
        }

        User user = userOpt.get();

        // 사용자 상태 확인 추가
        if (user.getUserStatus() != User.UserStatus.ACTIVE) {
            String message = switch (user.getUserStatus()) {
                case INACTIVE -> "탈퇴 처리된 계정입니다.";
                case SUSPENSION -> "일시 정지된 계정입니다. 고객센터에 문의하세요.";
                case PERMANENTSTOP -> "영구 정지된 계정입니다.";
                case WITHDRAWN -> "이미 탈퇴된 계정입니다.";
                default -> "로그인 불가 상태입니다.";
            };
            return ResponseEntity.status(403).body(Map.of("message", message));
        }

        if (!passwordEncoder.matches(userPw, user.getUserPw())) {
            log.warn("비밀번호 불일치: {}", userId);
            return ResponseEntity.status(401).body(Map.of("message", "비밀번호가 일치하지 않습니다."));
        }

        String role = "ROLE_USER"; // 기본적으로 'ROLE_USER'
        String redirectUrl = "/";

        if ("ADMIN".equals(user.getUserType().toString())) {
            role = "ROLE_ADMIN"; // 'ADMIN'일 경우 'ROLE_ADMIN' 부여
            redirectUrl = "/admin/dashboard";
        }

        String accessToken = jwtUtil.generateAccessToken(
            user.getUserId(),
            user.getUserNo(),
            role,
            user.getUserNick()
        );
        log.info("로그인 성공! Access 토큰 발급됨: {}", accessToken);

        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());
        log.info("Refresh 토큰 발급됨: {}", refreshToken);

        return ResponseEntity.ok(Map.of(
            "message", "로그인 성공!",
            "accessToken", accessToken,
            "refreshToken", refreshToken,
            "redirectUrl", redirectUrl
        ));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkLogin(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("Authorization 헤더: {}", token);

        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7); // "Bearer" 제거
        } else {
            log.warn("토큰 없음 또는 형식 오류");
            return ResponseEntity.ok(Map.of("authenticated", false));
        }

        if (jwtUtil.validateToken(token)) {
            log.info("토큰 유효함");
            return ResponseEntity.ok(Map.of("authenticated", true));
        } else {
            log.warn("토큰 무효");
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
    }
}
