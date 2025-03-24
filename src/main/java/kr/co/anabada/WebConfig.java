package kr.co.anabada;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/login").setViewName("forward:/auth/login.html");
        registry.addViewController("/join").setViewName("forward:/auth/join.html");

        //HTML 자동 매핑
        registry.addViewController("/{path:[^\\.]+}").setViewName("forward:/{path}.html");
        registry.addViewController("/{folder}/{path:[^\\.]+}").setViewName("forward:/{folder}/{path}.html");
    }
}
