package kr.co.anabada.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.IndividualUserJoinRepository;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final IndividualUserJoinRepository userJoinRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    private static final long ACCESS_TOKEN_EXPIRY = 1000L * 60 * 15; // 15분
    private static final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 60 * 24 * 7; // 7일

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        String refreshToken = Jwts.builder()
                .setSubject(user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userId(user.getUserId())
                        .token(refreshToken)
                        .build()
        );

        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String reissueAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        Optional<RefreshToken> saved = refreshTokenRepository.findByToken(refreshToken);
        if (saved.isEmpty()) {
            throw new RuntimeException("이미 사용된 리프레시 토큰입니다.");
        }

        String userId = getUserIdFromToken(refreshToken);
        User user = userJoinRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));

        refreshTokenRepository.deleteByToken(refreshToken);

        return generateAccessToken(user);
    }

    public void addRefreshTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (REFRESH_TOKEN_EXPIRY / 1000));
        response.addCookie(cookie);
    }

}
