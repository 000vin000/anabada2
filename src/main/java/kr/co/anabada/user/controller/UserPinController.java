package kr.co.anabada.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.dto.UserPinRequestDto;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.IndividualUserLoginRepository;
import kr.co.anabada.user.service.IndividualUserInfoUpdateService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/pin")
@RequiredArgsConstructor
public class UserPinController {

    private final IndividualUserInfoUpdateService userUpdateService;
    private final JwtTokenHelper jwtTokenHelper;
    private final IndividualUserLoginRepository userLoginRepository;

    // 2차 비밀번호 등록 여부 확인
    @GetMapping("/status")
    public ResponseEntity<?> hasUserPin(HttpServletRequest request) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userInfo == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");

        User user = userLoginRepository.findById(userInfo.getUserNo())
                .orElseThrow(() -> new RuntimeException("유저 없음"));


        boolean hasPin = user.getUserPin() != null;
        return ResponseEntity.ok(Map.of("hasPin", hasPin));
    }

    // 2차 비밀번호 최초 등록
    @PostMapping
    public ResponseEntity<?> createUserPin(HttpServletRequest request,
                                           @RequestBody @Valid UserPinRequestDto dto) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userInfo == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");

        userUpdateService.createUserPin(userInfo.getUserNo(), dto.getUserPin());
        return ResponseEntity.ok().build();
    }

    // 2차 비밀번호 인증
    @PostMapping("/check")
    public ResponseEntity<?> checkUserPin(HttpServletRequest request,
                                          @RequestBody @Valid UserPinRequestDto dto) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userInfo == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");

        boolean result = userUpdateService.checkUserPin(userInfo.getUserNo(), dto.getUserPin());
        return result
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("2차 비밀번호가 일치하지 않습니다.");
    }

    // 회원 정보 조회
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        UserTokenInfo userInfo = jwtTokenHelper.getUserFromRequest(request);
        if (userInfo == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");

        User user = userLoginRepository.findByUserId(userInfo.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Map<String, Object> info = Map.of(
                "userName", user.getUserName(),
                "userNick", user.getUserNick(),
                "userPhone", user.getUserPhone(),
                "userAddress", user.getUserAddress()
        );
        return ResponseEntity.ok(info);
    }
}
