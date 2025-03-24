package kr.co.anabada.test;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtTestController {

    // 인증이 필요한 테스트 API
    @GetMapping("/test/protected")
    public String protectedTest() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userInfo = (auth != null) ? auth.getPrincipal().toString() : "익명 사용자";

        return "✅ [인증된 사용자 접근] 현재 사용자: " + userInfo;
    }

    // 인증 필요 없는 테스트 API
    @GetMapping("/test/public")
    public String publicTest() {
        return "✅ [공개 API] 누구나 접근 가능!";
    }
}
