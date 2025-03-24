package kr.co.anabada.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.user.entity.User; // User 엔티티 임포트

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh.expiration}") long refreshTokenExpiration
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }


    //Access Token 만들기
    public String generateAccessToken(String userId) {
        return generateToken(userId, accessTokenExpiration);
    }

    //Refresh Token 만들기
    public String generateRefreshToken(String userId) {
        return generateToken(userId, refreshTokenExpiration);
    }

    // 내부 공통 토큰 만들기
    private String generateToken(String userId, long expiration) {

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId()) // sub = userId
                .claim("userNo", user.getUserNo())
                .claim("userNick", user.getUserNick())
                .claim("userType", user.getUserType())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //AccessToken 추출
    public String extractAccessToken(HttpServletRequest request) {
        return extractToken(request); // 내부 위임
    }

    //내부 공통 토큰 추출

    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    //토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //사용자 아이디 추출
    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ 커스텀 claim 추출 (userNo, userType 등)
    public Object extractClaim(String token, String claimName) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(claimName);
    }
}
