package kr.co.anabada.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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

    //Access Token userId만
    public String generateAccessToken(String userId) {
        return generateToken(userId, null, null, null, accessTokenExpiration);
    }
    //Access Token
    public String generateAccessToken(String userId, Long userNo, String userType, String nickname) {
        return generateToken(userId, userNo, userType, nickname, accessTokenExpiration);
    }

    //Refresh Token
    public String generateRefreshToken(String userId) {
        return generateToken(userId, null, null, null, refreshTokenExpiration);
    }

    //공통 토큰
    private String generateToken(String userId, Long userNo, String userType, String nickname, long expiration) {
        var builder = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration));

        // 커스텀 claim
        if (userNo != null) builder.claim("userNo", userNo);
        if (userType != null) builder.claim("userType", userType);
        if (nickname != null) builder.claim("nickname", nickname);

        return builder
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    

    
    //AccessToken 추출
    public String extractAccessToken(HttpServletRequest request) {
        return extractToken(request);
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

    //커스텀 claim 추출 (userNo, userType 등)
    public Object extractClaim(String token, String claimName) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(claimName);
    }
    
    //쿠키에서 토큰 꺼내기
    public String extractTokenFromCookie(HttpServletRequest request, String tokenName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
