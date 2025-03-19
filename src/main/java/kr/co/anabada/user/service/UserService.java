package kr.co.anabada.user.service;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // UserRepository 주입

    // 사용자 ID로 사용자 정보를 조회하는 메서드
    public User findById(Integer userNo) {
        Optional<User> user = userRepository.findById(userNo); // ID로 사용자 조회
        return user.orElse(null); // 사용자가 존재하면 반환, 없으면 null 반환
    }

    // 사용자 이메일로 사용자 정보를 조회하는 메서드
    public User findByEmail(String email) {
        return userRepository.findByUserEmail(email); // 이메일로 사용자 조회
    }

    // 사용자의 아이디로 사용자 정보를 조회하는 메서드
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId); // 아이디로 사용자 조회
    }

    // 사용자가 제출한 1:1 문의를 저장하는 메서드 (필요에 따라 추가)
    public void saveUser(User user) {
        userRepository.save(user); // 사용자를 저장
    }
}
