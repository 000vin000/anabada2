package kr.co.anabada.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kr.co.anabada.user.entity.User;

@Repository
public interface IndividualUserJoinRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByUserNick(String userNick);
}
