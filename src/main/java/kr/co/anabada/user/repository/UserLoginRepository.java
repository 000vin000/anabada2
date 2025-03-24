package kr.co.anabada.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.anabada.user.entity.User;
import java.util.Optional;

public interface UserLoginRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(String userId);
}
