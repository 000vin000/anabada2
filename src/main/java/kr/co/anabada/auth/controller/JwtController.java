package kr.co.anabada.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
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
public class JwtController {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final IndividualUserLoginService userLoginService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.getRefreshTokenFromRequest(request);  // 쿠키에서 Refresh Token 추출
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            log.warn("유효하지 않은 Refresh Token");
            return ResponseEntity.status(401).body("Refresh Token이 유효하지 않습니다.");
        }

        String userId = jwtUtil.extractUserId(refreshToken);

        RefreshToken saved = refreshTokenRepository.findById(userId).orElse(null);
        if (saved == null || !saved.getToken().equals(refreshToken)) {
            log.warn("DB에 저장된 Refresh Token과 일치하지 않음");
            return ResponseEntity.status(401).body("Refresh Token이 일치하지 않습니다.");
        }

        // ✅ userId로 사용자 다시 조회
        User user = userLoginService.findByUserId(userId).orElse(null);
        if (user == null) {
            log.warn("재발급 중 사용자 조회 실패: {}", userId);
            return ResponseEntity.status(401).body("사용자 정보를 찾을 수 없습니다.");
        }

        // ✅ 4개 정보 넣고 AccessToken 재발급
        String newAccessToken = jwtUtil.generateAccessToken(
                user.getUserId(),
                user.getUserNo(),
                user.getUserType().name(),
                user.getUserNick()
        );

        log.info("Access Token 재발급 성공 - userId: {}", userId);

        return ResponseEntity.ok().body(
                Map.of("accessToken", newAccessToken)
        );
    }
}
