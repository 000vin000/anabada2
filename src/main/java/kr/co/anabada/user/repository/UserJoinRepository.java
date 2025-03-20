package kr.co.anabada.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.anabada.user.entity.User;
import java.util.Optional;

public interface UserJoinRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserEmail(String userEmail);
}
