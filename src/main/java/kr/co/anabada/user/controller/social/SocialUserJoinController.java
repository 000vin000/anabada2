package kr.co.anabada.user.controller.social;

import jakarta.validation.Valid;
import kr.co.anabada.user.dto.social.SocialUserJoinDTO;
import kr.co.anabada.user.service.social.SocialUserJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/social")
@RequiredArgsConstructor
@Slf4j
public class SocialUserJoinController {

    private final SocialUserJoinService socialUserJoinService;

    @PostMapping("/join")
    public ResponseEntity<?> joinSocialUser(@Valid @RequestBody SocialUserJoinDTO dto) {
        try {
            log.info("소셜 회원가입 요청: {}", dto);

            String token = socialUserJoinService.registerSocialUserAndGenerateToken(dto);

            return ResponseEntity.ok(Map.of(
                "message", "소셜 회원가입 완료",
                "token", token
            ));
        } catch (IllegalArgumentException e) {
            log.warn("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
