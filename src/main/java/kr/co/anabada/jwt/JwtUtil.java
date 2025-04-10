package kr.co.anabada.jwt;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh.expiration}") long refreshTokenExpiration
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(String userId) {
        return generateToken(userId, null, null, null, accessTokenExpiration);
    }

    public String generateAccessToken(String userId, Integer userNo, String userType, String nickname) {
        return generateToken(userId, userNo, userType, nickname, accessTokenExpiration);
    }

    public String generateRefreshToken(String userId) {
        return generateToken(userId, null, null, null, refreshTokenExpiration);
    }

    private String generateToken(String userId, Integer userNo, String userType, String nickname, long expiration) {
        var builder = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration));

        if (userNo != null) builder.claim("userNo", userNo);
        if (userType != null) builder.claim("roles", Collections.singletonList("ROLE_" + userType));
        if (nickname != null) builder.claim("nickname", nickname);

        return builder
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

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

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
  //헤더에서 토큰 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        String tokenValue = req.getHeader(AUTHORIZATION_HEADER);
        if (tokenValue != null && !tokenValue.isEmpty()) {
            return URLDecoder.decode(tokenValue, StandardCharsets.UTF_8);
        }
        return null;
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Object extractClaim(String token, String claimName) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(claimName);
    }
    
    public List<String> extractRoles(String token) {
        Object roles = extractClaim(token, "roles");
        if (roles instanceof List) {
            return ((List<?>) roles).stream().map(Object::toString).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    

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
    
    // userNo 추출 ( 정빈 추가 )
    public UserTokenInfo getUserNoFromRequest(HttpServletRequest req) {
        // 요청에서 토큰 추출
        String token = extractToken(req);
        
        // 토큰이 없거나 유효하지 않으면 null 반환
        if (token == null || !validateToken(token)) {
            return null;
        }

        // 토큰에서 사용자 정보 추출
        String userId = extractUserId(token);
        Integer userNo = (Integer) extractClaim(token, "userNo");
        String userType = (String) extractClaim(token, "userType");
        String nickname = (String) extractClaim(token, "nickname");

        // 사용자 정보를 담은 UserTokenInfo 객체 반환
        return new UserTokenInfo(userId, userNo, userType, nickname);
    }

    // 추가된 메서드: 소셜 로그인 후 사용자 정보로 JWT 토큰 생성
    public String generateSocialAccessToken(String userId, String userType, String nickname) {
        return generateToken(userId, null, userType, nickname, accessTokenExpiration);
    }
}
