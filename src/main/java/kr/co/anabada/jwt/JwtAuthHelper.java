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
        
        System.out.println("token1: " + token);
        
        if (token != null && jwtUtil.validateToken(token)) {
            String userId = jwtUtil.extractUserId(token);
            Integer userNo = toInteger(jwtUtil.extractClaim(token, "userNo"));  // 🔁 Integer
            String userType = toString(jwtUtil.extractClaim(token, "userType"));
            String nickname = toString(jwtUtil.extractClaim(token, "nickname"));

            UserTokenInfo userTokenInfo = new UserTokenInfo(userId, userNo, userType, nickname);
            
            System.out.println("userTokenInfo: " + userTokenInfo);
            
            return userTokenInfo;
        }
        return null;
    }

    private Integer toInteger(Object value) {
        if (value instanceof Number) return ((Number) value).intValue(); // ✅ 안전 변환
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
