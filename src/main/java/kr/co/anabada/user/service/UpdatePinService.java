package kr.co.anabada.user.service;

import kr.co.anabada.user.dto.UserPinUpdateRequestDto;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void updatePin(Integer userNo, String userTypeFromToken, UserPinUpdateRequestDto dto) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 실제 user 엔티티의 타입 기준으로 확인
        User.UserType userType = user.getUserType();

        switch (userType) {
            case INDIVIDUAL, SOCIAL -> {
                user.setUserPin(passwordEncoder.encode(dto.getNewPin()));
                userRepository.save(user);
            }
            case BRAND -> throw new UnsupportedOperationException("브랜드 회원은 아직 지원되지 않습니다.");
            default -> throw new AccessDeniedException("해당 회원 유형은 2차 비밀번호 변경을 지원하지 않습니다.");
        }
    }
}
