package kr.co.anabada.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private static final String REFRESH_TOKEN_NAME = "refreshToken";

    // Refresh Token을 쿠키에 추가
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAgeSeconds) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_NAME, refreshToken);
        cookie.setHttpOnly(true);  
        cookie.setSecure(false);  
        cookie.setPath("/");     // 전체 경로에서 접근 가능하도록 설정
        cookie.setMaxAge((int) maxAgeSeconds);  // 쿠키 만료 시간 설정

        // 디버깅 로그 추가
        System.out.println("쿠키에 저장된 Refresh Token: " + refreshToken);

        response.addCookie(cookie);
    }

    // 쿠키에서 Refresh Token 추출
    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            System.out.println("No cookies found in request.");
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (REFRESH_TOKEN_NAME.equals(cookie.getName())) {
                System.out.println("쿠키에서 추출된 Refresh Token: " + cookie.getValue());
                return cookie.getValue();  // 쿠키에서 Refresh Token 값을 반환
            }
        }
        
        System.out.println("Refresh Token 쿠키가 없습니다.");
        return null;  // 쿠키에 해당 값이 없으면 null 반환
    }

    // Refresh Token 쿠키 삭제
    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_NAME, null);
        cookie.setMaxAge(0);  // 쿠키 만료시간을 0으로 설정하여 삭제
        cookie.setPath("/");  // 경로 설정
        response.addCookie(cookie);

        // 디버깅 로그 추가
        System.out.println("Refresh Token 쿠키 삭제 완료.");
    }
}
