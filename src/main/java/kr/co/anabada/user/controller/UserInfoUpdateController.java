package kr.co.anabada.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.dto.UserInfoUpdateRequestDto;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserLoginRepository;
import kr.co.anabada.user.service.UserUpdateService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/update/info")
@RequiredArgsConstructor
public class UserInfoUpdateController {

    private final UserUpdateService userUpdateService;
    private final UserLoginRepository userLoginRepository;
    private final JwtTokenHelper jwtTokenHelper;

    @PutMapping
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request,
                                            @RequestBody @Valid UserInfoUpdateRequestDto dto) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userInfo == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");

        userUpdateService.updateUserInfo(userInfo.getUserNo(), dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> updateInfo(HttpServletRequest request) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userInfo == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");

        User user = userLoginRepository.findByUserId(userInfo.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        return ResponseEntity.ok(user.getUserEmail());
    }
}
