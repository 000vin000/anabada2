package kr.co.anabada.user.repository;

import kr.co.anabada.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndividualUserUpdateRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(String userId);
}
