package kr.co.anabada.user.controller.withdraw;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.dto.withdraw.UserWithdrawRequestDto;
import kr.co.anabada.user.service.withdraw.UserWithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserWithdrawController {

    private final UserWithdrawService withdrawService;
    private final JwtTokenHelper jwtTokenHelper;

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdrawUser(@RequestBody UserWithdrawRequestDto dto, HttpServletRequest request) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userInfo == null || userInfo.getUserNo() == null) {
            return ResponseEntity.badRequest().body("잘못된 인증 정보입니다.");
        }

        Integer userNo = userInfo.getUserNo();
        withdrawService.withdraw(userNo, dto);
        return ResponseEntity.ok().body("회원 탈퇴가 정상적으로 처리되었습니다.");
    }
}
