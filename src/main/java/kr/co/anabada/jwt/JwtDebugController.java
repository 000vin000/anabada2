package kr.co.anabada.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/debug/jwt")
@RequiredArgsConstructor
public class JwtDebugController {

    private final JwtUtil jwtUtil;

    //Access Token + Refresh Token 확인 API
    @GetMapping("/check")
    public ResponseEntity<?> checkToken(HttpServletRequest request) {
        // Access Token: 헤더에서 추출
        String accessToken = jwtUtil.extractAccessToken(request);
        boolean isAccessValid = accessToken != null && jwtUtil.validateToken(accessToken);

        // Refresh Token: 쿠키에서 추출
        String refreshToken = jwtUtil.extractTokenFromCookie(request, "refreshToken");
        boolean isRefreshValid = refreshToken != null && jwtUtil.validateToken(refreshToken);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "accessValid", isAccessValid,
                "refreshToken", refreshToken,
                "refreshValid", isRefreshValid
        ));
    }

    //Access Token의 사용자 정보 출력
    @GetMapping("/access-info")
    public ResponseEntity<?> getAccessTokenInfo(HttpServletRequest request) {
        String token = jwtUtil.extractAccessToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Access Token이 없거나 유효하지 않음");
        }

        return ResponseEntity.ok(Map.of(
                "userId", jwtUtil.extractUserId(token),
                "userNo", jwtUtil.extractClaim(token, "userNo"),
                "userType", jwtUtil.extractClaim(token, "userType"),
                "nickname", jwtUtil.extractClaim(token, "nickname")
        ));
    }

    //Refresh Token에서 userId 추출 (다른 정보는 없음)
    @GetMapping("/refresh-info")
    public ResponseEntity<?> getRefreshTokenInfo(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromCookie(request, "refreshToken");
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Refresh Token이 없거나 유효하지 않음");
        }

        return ResponseEntity.ok(Map.of(
                "userId", jwtUtil.extractUserId(token)
        ));
    }
}
