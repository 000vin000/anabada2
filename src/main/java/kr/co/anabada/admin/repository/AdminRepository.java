package kr.co.anabada.admin.repository;

import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.user.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
   Admin findByUser(User user);
}
