package kr.co.anabada.admin.repository;

import kr.co.anabada.admin.entity.Warn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarnRepository extends JpaRepository<Warn, Integer> {
	
}
