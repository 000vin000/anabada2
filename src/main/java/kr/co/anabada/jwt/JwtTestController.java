package kr.co.anabada.jwt;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class JwtTestController {

    private final JwtUtil jwtUtil;

    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(HttpServletRequest request) {
        // 헤더에서 토큰 추출
        String tokenFromHeader = jwtUtil.extractAccessToken(request);
        System.out.println("🔍 헤더 토큰: " + tokenFromHeader);

        // 쿠키에서 토큰 추출
        String tokenFromCookie = jwtUtil.extractTokenFromCookie(request, "accessToken");
        System.out.println("🍪 쿠키 토큰: " + tokenFromCookie);

        // 만약 헤더에서 토큰이 없으면 쿠키에서 찾기
        String token = tokenFromHeader != null ? tokenFromHeader : tokenFromCookie;

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ 토큰 없음");
        }

        // 토큰 유효성 검사
        boolean isValid = jwtUtil.validateToken(token);
        System.out.println("✅ 유효한가? " + isValid);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ 토큰 무효");
        }

        // 토큰에서 userId 추출
        String userId = jwtUtil.extractUserId(token);
        System.out.println("🧑 userId: " + userId);

        // 최종적으로 응답 데이터 반환
        return ResponseEntity.ok(Map.of(
            "tokenFromHeader", tokenFromHeader,
            "tokenFromCookie", tokenFromCookie,
            "isValid", isValid,
            "userId", userId
        ));
    }

}
