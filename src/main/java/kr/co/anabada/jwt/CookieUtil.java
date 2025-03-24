package kr.co.anabada.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private static final String REFRESH_TOKEN_NAME = "refreshToken";

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAgeSeconds) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_NAME, refreshToken);
        cookie.setHttpOnly(true);  
        cookie.setSecure(true);          
        cookie.setPath("/");              
        cookie.setMaxAge((int) maxAgeSeconds); 
        response.addCookie(cookie);
    }

    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (REFRESH_TOKEN_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_NAME, null);
        cookie.setMaxAge(0); 
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
