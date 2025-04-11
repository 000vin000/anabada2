package kr.co.anabada.user.service;

import kr.co.anabada.user.dto.UserPinUpdateRequestDto;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.IndividualUserJoinRepository;
import kr.co.anabada.user.repository.social.SocialUserJoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePinService {

    private final IndividualUserJoinRepository individualRepo;
    private final SocialUserJoinRepository socialRepo;
    // private final BrandUserJoinRepository brandRepo; // ❗ 확장 가능, 현재 미사용
    private final PasswordEncoder passwordEncoder;

    public void updatePin(Integer userNo, String userType, UserPinUpdateRequestDto dto) {
        User user;

        switch (userType) {
            case "ROLE_INDIVIDUAL" -> user = individualRepo.findById(userNo)
                    .orElseThrow(() -> new IllegalArgumentException("개인 회원을 찾을 수 없습니다."));
            case "ROLE_SOCIAL" -> user = socialRepo.findById(userNo)
                    .orElseThrow(() -> new IllegalArgumentException("소셜 회원을 찾을 수 없습니다."));
            case "ROLE_BRAND" -> throw new UnsupportedOperationException("브랜드 회원은 아직 지원되지 않습니다.");
            default -> throw new AccessDeniedException("해당 회원 유형은 2차 비밀번호 변경을 지원하지 않습니다.");
        }

        user.setUserPin(passwordEncoder.encode(dto.getNewPin()));

        switch (userType) {
            case "ROLE_INDIVIDUAL" -> individualRepo.save(user);
            case "ROLE_SOCIAL" -> socialRepo.save(user);
            case "ROLE_BRAND" -> {
                // 향후 brandRepo.save(user); 추가 예정
            }
        }
    }
}
