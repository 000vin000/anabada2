package kr.co.anabada;

import kr.co.anabada.jwt.JwtAuthenticationFilter;
import kr.co.anabada.jwt.JwtUtil;
import kr.co.anabada.user.service.UserDetailsServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
            		
                // 인증 없이 접근 가능한 공개 경로들
                .requestMatchers(
                    "/", "/login", "/join",
                    "/auth/**", "/userjoin/**", "/userlogin/**",
                    "/item/detail/**"
                ).permitAll()

                // 증이 반드시 필요한 경로들
                .requestMatchers(
                    "/user/mypage", "/user/update"  // 예: 마이페이지, 회원정보 수정
                ).authenticated()

                // 외의 모든 요청은 허용
                .anyRequest().permitAll()
            )

            .formLogin(form -> form
                .loginPage("/auth/login.html").permitAll()
            )

            // JWT 인증 필터 등록
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtUtil, userDetailsService),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
