package kr.co.anabada.user.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        HttpSession session = httpRequest.getSession(false);

        // 로그인이 필요한 페이지 목록
        String[] restrictedPages = {"/mypage", "/detail/q"}; //{"/mypage", "/qna", "/sell"}

        boolean needsAuthentication = false;
        for (String page : restrictedPages) {
            if (requestURI.startsWith(httpRequest.getContextPath() + page)) {
                needsAuthentication = true;
                break;
            }
        }

        if (needsAuthentication) {
            if (session == null || session.getAttribute("loggedInUser") == null) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/user/login");
                return;
            }
        }

        // 모든 요청에 대해 로그인 상태를 request 속성으로 설정
        if (session != null && session.getAttribute("loggedInUser") != null) {
            request.setAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 필터 종료 시 필요한 작업
    }
}
