package kr.co.anabada.admin.repository;

import kr.co.anabada.admin.entity.Warn;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarnRepository extends JpaRepository<Warn, Integer> {
	 @EntityGraph(attributePaths = "user")  // User를 즉시 로딩
	 List<Warn> findAll();
}
