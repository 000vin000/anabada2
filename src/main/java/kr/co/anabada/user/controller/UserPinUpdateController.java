package kr.co.anabada.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.user.dto.UserPinUpdateRequestDto;
import kr.co.anabada.user.service.UpdatePinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kr.co.anabada.jwt.UserTokenInfo;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/update/pin")
public class UserPinUpdateController {

    private final UpdatePinService updatePinService;
    private final JwtTokenHelper jwtTokenHelper;

    @PutMapping
    public ResponseEntity<String> updatePin(@RequestBody @Valid UserPinUpdateRequestDto dto,
                                            HttpServletRequest request) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserNoFromRequest(request); // ✅ 인스턴스로 호출

        if (userInfo == null) {
            return ResponseEntity.status(401).body("인증 정보가 유효하지 않습니다.");
        }

        Integer userNo = userInfo.getUserNo();
        String userType = userInfo.getUserType();

        updatePinService.updatePin(userNo, userType, dto);
        return ResponseEntity.ok("2차 비밀번호가 성공적으로 변경되었습니다.");
    }
}
