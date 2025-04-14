package kr.co.anabada.auth.handler;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
	                                     HttpServletResponse response,
	                                     AuthenticationException exception) throws IOException, ServletException {
	    log.warn("소셜 로그인 실패: {}", exception.getMessage());

	    String displayMessage;
	    if (exception instanceof OAuth2AuthenticationException oAuthEx) {
	        String reason = oAuthEx.getError().getDescription(); // ✔ 핵심: 깔끔한 설명 추출
	        displayMessage = (reason != null) ? reason : "소셜 로그인 중 오류가 발생했습니다.";
	    } else {
	        displayMessage = "소셜 로그인 중 오류가 발생했습니다.";
	    }

	    String encodedMessage = URLEncoder.encode(displayMessage, StandardCharsets.UTF_8);
	    String redirectUrl = "/auth/login/individual/IndividualLogin.html?error=" + encodedMessage;

	    response.sendRedirect(redirectUrl);
	}


}
