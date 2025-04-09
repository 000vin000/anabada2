package kr.co.anabada.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.anabada.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("OAuth2 로그인 성공");
        log.info("OAuth2 Attributes: {}", attributes);

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        boolean isNew = false;
        Object isNewAttr = attributes.get("isNew");
        if (isNewAttr instanceof Boolean) {
            isNew = (Boolean) isNewAttr;
        } else if (isNewAttr instanceof String) {
            isNew = Boolean.parseBoolean((String) isNewAttr);
        }

        log.info("isNew 값: {}", isNew);

        if (isNew) {
            //URL 인코딩 처리
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
            String redirectUrl = "http://localhost:8080/auth/join/social/socialGoogleJoin.html?email=" + encodedEmail + "&name=" + encodedName;

            log.info("신규 사용자 리다이렉트: {}", redirectUrl);
            response.sendRedirect(redirectUrl);

        } else {
            Integer userNo = (Integer) attributes.get("userNo");
            String nickname = (String) attributes.get("name");
            String userType = "SOCIAL";

            String token = jwtUtil.generateAccessToken(email, userNo, userType, nickname);
            String redirectUrl = "http://localhost:8080/auth/login/social/socialLoginSuccess.html?token=" + token;

            log.info("기존 유저 JWT 발급: {}", token);
            log.info("기존 유저 리다이렉트: {}", redirectUrl);

            response.sendRedirect(redirectUrl);
        }
    }
}
