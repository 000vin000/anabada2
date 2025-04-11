package kr.co.anabada.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.dto.UpdatePasswordDTO;
import kr.co.anabada.user.service.UpdatePasswordService;
import kr.co.anabada.user.entity.User.UserType;

@RestController
@RequestMapping("/user/update/password")
@RequiredArgsConstructor
public class UpdatePasswordController {

    private final UpdatePasswordService updatePasswordService;
    private final JwtTokenHelper jwtTokenHelper;

    @PutMapping
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto,
                                            HttpServletRequest request) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);

        if (userInfo == null || userInfo.getUserNo() == null) {
            return ResponseEntity.status(401).body("유효하지 않은 사용자입니다.");
        }

        if ("SOCIAL".equals(userInfo.getUserType())) {
            return ResponseEntity.status(403).body("소셜 로그인 계정은 비밀번호 변경이 불가능합니다.");
        }


        try {
            updatePasswordService.updatePassword(userInfo.getUserNo(), dto);
            return ResponseEntity.ok("비밀번호가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }
}
