package kr.co.anabada.user.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.anabada.user.entity.EmailVerificationInfo;

public class EmailVerificationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        // 회원가입 페이지에 대한 요청인 경우에만 필터 적용
        if (httpRequest.getRequestURI().equals("/user/join")) {
            EmailVerificationInfo verificationInfo = (EmailVerificationInfo) session.getAttribute("emailVerificationInfo");

            if (verificationInfo == null || isVerificationExpired(verificationInfo)) {
                // 이메일 인증이 없거나 만료된 경우 이메일 인증 페이지로 리다이렉트
                httpResponse.sendRedirect("/email/verification");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isVerificationExpired(EmailVerificationInfo info) {
        return LocalDateTime.now().isAfter(info.getVerificationTime().plusMinutes(3));
    }
}