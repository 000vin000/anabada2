package kr.co.anabada.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserJoinRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenHelper {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserJoinRepository userJoinRepository;

    //AccessToken에서 유저 정보 추출
    public UserTokenInfo extractUserInfoFromAccessToken(String token) {
        if (!jwtUtil.validateToken(token)) return null;

        String userId = jwtUtil.extractUserId(token);
        Integer userNo = toInteger(jwtUtil.extractClaim(token, "userNo"));
        String userType = toString(jwtUtil.extractClaim(token, "userType"));
        String nickname = toString(jwtUtil.extractClaim(token, "nickname"));

        return new UserTokenInfo(userId, userNo, userType, nickname);
    }

    // RefreshToken으로 유저 조회
    public Optional<User> findUserByRefreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) return Optional.empty();

        String userId = jwtUtil.extractUserId(refreshToken);

        Optional<RefreshToken> savedToken = refreshTokenRepository.findById(userId);
        if (savedToken.isEmpty() || !savedToken.get().getToken().equals(refreshToken)) {
            return Optional.empty();
        }

        return userJoinRepository.findByUserId(userId);
    }

    //HttpServletRequest에서 AccessToken으로 바로 User 정보 추출
    public UserTokenInfo getUserFromRequest(HttpServletRequest request) {
        String token = jwtUtil.extractAccessToken(request);
        return extractUserInfoFromAccessToken(token);
    }

    // internal helper methods
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
