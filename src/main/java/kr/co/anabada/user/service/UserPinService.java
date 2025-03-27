package kr.co.anabada.user.service;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPinService {

    private final UserLoginRepository userLoginRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUserPin(Integer userNo, String rawPin) {
        User user = userLoginRepository.findById(userNo)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 존재하지 않습니다."));

        if (user.getUserPin() != null) {
            throw new IllegalStateException("이미 등록된 2차 비밀번호가 있습니다.");
        }

        String encodedPin = passwordEncoder.encode(rawPin);
        user.setUserPin(encodedPin);
        userLoginRepository.save(user);
    }

    public boolean checkUserPin(Integer userNo, String inputPin) {
        User user = userLoginRepository.findById(userNo)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 존재하지 않습니다."));

        return passwordEncoder.matches(inputPin, user.getUserPin());
    }
} 
