package kr.co.anabada.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Base64;

@Component
public class JwtUtil {

    // ✅ Base64 인코딩된 256비트(32바이트) 키 사용 (환경변수로 관리 권장)
    private static final String SECRET_KEY = Base64.getEncoder().encodeToString("your-very-strong-secret-key-which-is-at-least-32-bytes".getBytes());
    private static final long EXPIRATION_TIME = 1000 * 60 * 30; // 30분

    // ✅ 서명 키 생성
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ JWT 토큰 생성
    public String generateToken(String userId) {
        String token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("Generated Token: " + token);
        return token;
    }

    // ✅ 요청에서 JWT 토큰 추출
    public String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출
        }
        return null;
    }


    // ✅ JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            System.out.println("Token is valid.");
            return true;
        } catch (Exception e) {
            System.out.println("Invalid Token: " + e.getMessage());
            return false;
        }
    }

    // ✅ JWT 토큰에서 사용자 ID 추출
    public String extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        System.out.println("Extracted UserId: " + claims.getSubject());
        return claims.getSubject();
    }
}
