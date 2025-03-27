package kr.co.anabada.user.repository;

import kr.co.anabada.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserLoginRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(String userId); //반환 타입을 Optional로 변경
}
