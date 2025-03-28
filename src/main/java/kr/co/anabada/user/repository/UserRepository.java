package kr.co.anabada.user.repository;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.Seller;
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



}

