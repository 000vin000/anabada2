package kr.co.anabada.user.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import kr.co.anabada.user.dto.UserJoinDTO;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserJoinRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserJoinService {

    @Autowired
    private UserJoinRepository userJoinRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    //아이디 중복 체크
    public boolean isUserIdAvailable(String userId) {
        return userJoinRepository.findByUserId(userId).isEmpty();
    }

    //닉네임 중복 체크
    public boolean isUserNickAvailable(String userNick) {
        return userJoinRepository.findByUserNick(userNick).isEmpty();
    }

    public void joinUser(UserJoinDTO userJoinDTO) {
        if (userJoinRepository.findByUserId(userJoinDTO.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        
        if (userJoinRepository.findByUserNick(userJoinDTO.getUserNick()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        if (userJoinRepository.findByUserEmail(userJoinDTO.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        String encryptedPassword = passwordEncoder.encode(userJoinDTO.getUserPw());

        // 주소 합치기
        String fullAddress = userJoinDTO.getBaseAddress() + " " + userJoinDTO.getDetailAddress();

        // 현재 시간 설정
        LocalDateTime now = LocalDateTime.now();

        // 회원 생성
        User user = User.builder()
                .userId(userJoinDTO.getUserId())
                .userPw(encryptedPassword)
                .userName(userJoinDTO.getUserName())
                .userNick(userJoinDTO.getUserNick())
                .userEmail(userJoinDTO.getUserEmail())
                .userPhone(userJoinDTO.getUserPhone())
                .userAddress(fullAddress)
                .userType(userJoinDTO.getUserType())
                .userStatus(User.UserStatus.ACTIVE)
                .userCreatedDate(LocalDateTime.now())
                .userUpdatedDate(LocalDateTime.now())
                .userWarnCnt((byte) 0)
                .build();

        // 로그 출력
        log.info("회원가입 요청: {}", user);

        userJoinRepository.save(user);
        log.info("회원가입 완료! userUpdatedDate={}", user.getUserUpdatedDate());
    }
}
