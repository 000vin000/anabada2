package kr.co.anabada.user.repository.social;

import kr.co.anabada.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialUserLoginRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserEmail(String userEmail);
}
