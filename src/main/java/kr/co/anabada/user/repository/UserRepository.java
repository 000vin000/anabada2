package kr.co.anabada.user.repository;

import kr.co.anabada.user.entity.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 이메일로 사용자 조회
    User findByUserEmail(String email);

    // 사용자 ID로 사용자 조회
    User findByUserId(String userId);

    // 사용자 번호(PK)로 사용자 조회
    Optional<User> findByUserNo(Integer userNo);

    // 사용자 번호 + 타입으로 조회
    Optional<User> findByUserNoAndUserType(Integer userNo, User.UserType userType);

    // 2차 비밀번호 등록 여부 확인
    boolean existsByUserNoAndUserPinIsNotNull(Integer userNo);

    // 기타 필요한 조건이 생기면 여기에 추가
}
