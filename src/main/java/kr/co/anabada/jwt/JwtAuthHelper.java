package kr.co.anabada.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthHelper {

    private final JwtUtil jwtUtil;

    public UserTokenInfo getUserFromRequest(HttpServletRequest request) {
        String token = jwtUtil.extractAccessToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            String userId = jwtUtil.extractUserId(token);
            Integer userNo = toInteger(jwtUtil.extractClaim(token, "userNo"));
            String userType = toString(jwtUtil.extractClaim(token, "userType"));
            String nickname = toString(jwtUtil.extractClaim(token, "nickname"));

            return new UserTokenInfo(userId, userNo, userType, nickname);
        }
        return null;
    }

    private Integer toInteger(Object value) {
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private String toString(Object value) {
        return value != null ? value.toString() : null;
    }
}
