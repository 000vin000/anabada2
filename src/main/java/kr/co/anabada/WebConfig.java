package kr.co.anabada;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 로그인
        registry.addViewController("/login")
                .setViewName("forward:/auth/login/individual/IndividualLogin.html");

        // 회원가입 - 유형 선택
        registry.addViewController("/join")
                .setViewName("forward:/auth/join/join.html");

        // 개인 회원가입 - 이메일 인증 단계
        registry.addViewController("/join/individual/emailAuth")
                .setViewName("forward:/auth/join/individual/emailAuth.html");
        
        // 개인 회원가입 - 상세 정보 입력
        registry.addViewController("/join/individual/details")
                .setViewName("forward:/auth/join/individual/IndividualJoin.html");

    
    }
}
