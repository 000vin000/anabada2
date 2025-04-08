package kr.co.anabada.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.IndividualUserJoinRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenHelper {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final IndividualUserJoinRepository userJoinRepository;

    //AccessToken에서 유저 정보 추출
//    public UserTokenInfo extractUserInfoFromAccessToken(String token) {
//        if (!jwtUtil.validateToken(token)) return null;

//        String userId = jwtUtil.extractUserId(token);
//        Integer userNo = toInteger(jwtUtil.extractClaim(token, "userNo"));
//        String userType = toString(jwtUtil.extractClaim(token, "userType"));
//        String nickname = toString(jwtUtil.extractClaim(token, "nickname"));

//        return new UserTokenInfo(userId, userNo, userType, nickname);
//    }

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
    
    public List<String> getRolesFromRequest(HttpServletRequest request) {
        String token = jwtUtil.extractAccessToken(request);
        if (!jwtUtil.validateToken(token)) {
            return List.of(); // 토큰이 유효하지 않으면 빈 리스트 반환
        }

        Object rolesObj = jwtUtil.extractClaim(token, "roles"); 
        if (rolesObj instanceof List<?>) {
            return (List<String>) rolesObj;
        } else if (rolesObj instanceof String) {
            return List.of(rolesObj.toString());
        } else {
            return List.of();
        }
    }
    // userNo 추출 ( 정빈 추가 )
    public UserTokenInfo extractUserInfoFromAccessToken(String token) {
        if (!jwtUtil.validateToken(token)) return null;

        String userId = jwtUtil.extractUserId(token);
        Integer userNo = toInteger(jwtUtil.extractClaim(token, "userNo"));
        String nickname = toString(jwtUtil.extractClaim(token, "nickname"));

        // 'roles' claim에서 첫 번째 role 가져오기
        List<String> roles = jwtUtil.extractRoles(token);
        String userType = roles.isEmpty() ? null : roles.get(0);

        return new UserTokenInfo(userId, userNo, userType, nickname);
    }
    
    public UserTokenInfo getUserNoFromRequest(HttpServletRequest req) {
        String token = jwtUtil.extractToken(req);
        if (token == null || !jwtUtil.validateToken(token)) {
            return null;
        }

        // 수정된 부분: roles -> userType
        String userId = jwtUtil.extractUserId(token);
        Integer userNo = (Integer) jwtUtil.extractClaim(token, "userNo");
        String nickname = (String) jwtUtil.extractClaim(token, "nickname");

        // roles 클레임에서 userType 추출
        List<String> roles = jwtUtil.extractRoles(token);
        String userType = roles.isEmpty() ? null : roles.get(0);

        return new UserTokenInfo(userId, userNo, userType, nickname);
    }
    



}
