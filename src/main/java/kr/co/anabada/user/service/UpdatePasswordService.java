package kr.co.anabada.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import kr.co.anabada.user.dto.UpdatePasswordDTO;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.IndividualUserJoinRepository;

@Service
@RequiredArgsConstructor
public class UpdatePasswordService {

    private final IndividualUserJoinRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void updatePassword(Integer userNo, UpdatePasswordDTO dto) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getUserPw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setUserPw(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
