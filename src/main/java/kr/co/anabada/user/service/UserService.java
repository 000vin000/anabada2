package kr.co.anabada.user.service;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 사용자 ID로 조회
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    // 사용자 이메일로 조회
    public User findByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    // 사용자 저장
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
