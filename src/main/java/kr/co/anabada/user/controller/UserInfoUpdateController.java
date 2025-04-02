package kr.co.anabada.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.dto.UserInfoUpdateRequestDto;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserUpdateRepository;
import kr.co.anabada.user.service.UserInfoUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/update")
@RequiredArgsConstructor
@Slf4j
public class UserInfoUpdateController {

    private final UserInfoUpdateService userUpdateService;
    private final UserUpdateRepository userUpdateRepository;
    private final JwtTokenHelper jwtTokenHelper;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userInfo == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");

        User user = userUpdateRepository.findByUserId(userInfo.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        log.info("회원정보 조회 요청: userId = {}", user.getUserId());

        String[] addressParts = user.getUserAddress().split("::");
        String baseAddress = addressParts.length > 0 ? addressParts[0] : "";
        String detailAddress = addressParts.length > 1 ? addressParts[1] : "";

        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", user.getUserId());
        userData.put("userEmail", user.getUserEmail());
        userData.put("userName", user.getUserName());
        userData.put("userNick", user.getUserNick());
        userData.put("userPhone", user.getUserPhone());
        userData.put("baseAddress", baseAddress);
        userData.put("detailAddress", detailAddress);

        return ResponseEntity.ok(userData);
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request,
                                            @RequestBody UserInfoUpdateRequestDto dto) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userInfo == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");

        log.info("회원정보 수정 요청: userNo = {}, 수정할 닉네임: {}", userInfo.getUserNo(), dto.getUserNick());
        userUpdateService.updateUserInfo(userInfo.getUserNo(), dto);
        return ResponseEntity.ok().build();
    }
}
