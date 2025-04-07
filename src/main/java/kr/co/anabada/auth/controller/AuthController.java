package kr.co.anabada.auth.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.anabada.jwt.CookieUtil;
import kr.co.anabada.jwt.JwtUtil;
import kr.co.anabada.jwt.RefreshToken;
import kr.co.anabada.jwt.RefreshTokenRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.IndividualUserLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final IndividualUserLoginService userLoginService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestData,
                                   HttpServletResponse response) {
        String userId = requestData.get("userId");
        String userPw = requestData.get("userPw");

        Optional<User> userOpt = userLoginService.findByUserId(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "존재하지 않는 사용자입니다."));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(userPw, user.getUserPw())) {
            return ResponseEntity.status(401).body(Map.of("message", "비밀번호가 일치하지 않습니다."));
        }

        String accessToken = jwtUtil.generateAccessToken(
                user.getUserId(),
                user.getUserNo(),
                user.getUserType().name(),
                user.getUserNick()
        );

        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getUserId())
                .token(refreshToken)
                .build());

        cookieUtil.addRefreshTokenCookie(response, refreshToken, 7 * 24 * 60 * 60);

        //accessToken을 응답 헤더에 추가
        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok(Map.of(
                "message", "로그인 성공",
                "accessToken", accessToken,
                "redirectUrl", "/"
        ));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, Authentication auth) {
        String userId = (String) auth.getPrincipal(); 

        refreshTokenRepository.deleteById(userId);
        cookieUtil.deleteRefreshTokenCookie(response);  // 로그아웃 시 쿠키 삭제

        return ResponseEntity.ok(Map.of("message", "로그아웃 완료"));
    }
}
