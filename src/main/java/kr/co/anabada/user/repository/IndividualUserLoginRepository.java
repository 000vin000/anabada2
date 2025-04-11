package kr.co.anabada.user.repository;

import kr.co.anabada.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IndividualUserLoginRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(String userId); 
    Optional<User> findByUserNo(Integer userNo);

}
