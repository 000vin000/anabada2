package kr.co.anabada.jwt;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserJoinRepository;
import kr.co.anabada.user.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserJoinRepository userJoinRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        String token = jwtUtil.extractAccessToken(request);        

        if (token != null && jwtUtil.validateToken(token)) {
            String userId = jwtUtil.extractUserId(token);
            List<String> roles = jwtUtil.extractRoles(token);

            User user = userJoinRepository.findByUserId(userId)
                    .orElse(null);

            if (user != null) {
            	List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))  // "ROLE_" 접두사 추가
                        .collect(Collectors.toList());
            	
                UserDetailsImpl userDetails = new UserDetailsImpl(user);
               
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT 인증 완료 - userId: {}", userId, roles);
                
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth/")
                || path.equals("/")
                || path.equals("/login")
                || path.equals("/join")
                || path.startsWith("/item/detail/");
    }
}
