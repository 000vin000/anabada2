package kr.co.anabada.user.repository;

import kr.co.anabada.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 이메일로 사용자 조회
    User findByUserEmail(String email);

    // 사용자 ID로 사용자 조회
    User findByUserId(String userId);
}
