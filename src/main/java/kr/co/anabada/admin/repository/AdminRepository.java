package kr.co.anabada.admin.repository;

import kr.co.anabada.admin.entity.Admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	Optional<Admin> findByAdminId(String adminId);
}
