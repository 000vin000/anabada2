package kr.co.anabada.user.service.social;

import java.time.LocalDateTime;
import java.util.Optional;

import kr.co.anabada.jwt.JwtUtil;
import kr.co.anabada.user.dto.social.SocialUserJoinDTO;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.social.SocialUserJoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocialUserJoinService {

    private final SocialUserJoinRepository userRepository;
    private final JwtUtil jwtUtil;

    public String registerSocialUserAndGenerateToken(SocialUserJoinDTO dto) {
        Optional<User> exist = userRepository.findByUserEmail(dto.getUserEmail());
        if (exist.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String fullAddress = dto.getBaseAddress() + " " + dto.getDetailAddress();

        User user = User.builder()
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .userNick(dto.getUserNick())
                .userPhone(dto.getUserPhone())
                .userAddress(fullAddress)
                .userType(User.UserType.INDIVIDUAL)
                .userStatus(User.UserStatus.ACTIVE)
                .userCreatedDate(LocalDateTime.now())
                .userUpdatedDate(LocalDateTime.now())
                .userWarnCnt((byte) 0)
                .emailVerified(true)
                .userId("SOCIAL_" + System.currentTimeMillis())
                .userPw("SOCIAL_USER")
                .build();

        log.info("소셜 회원가입 사용자 정보: {}", user);
        userRepository.save(user);

        return jwtUtil.generateAccessToken(
                user.getUserEmail(),
                user.getUserNo(),
                user.getUserType().name(),
                user.getUserNick()
        );
    }
}
