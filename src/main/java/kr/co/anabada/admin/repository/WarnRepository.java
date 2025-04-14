package kr.co.anabada.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.admin.entity.Warn;
import kr.co.anabada.admin.entity.Warn.WarnStatus;
import kr.co.anabada.user.entity.User;

@Repository
public interface WarnRepository extends JpaRepository<Warn, Integer> {
	@EntityGraph(attributePaths = "user")  // User를 즉시 로딩
	List<Warn> findAll();

	// 신고 접수 목록 - 처리 전
	@Query("SELECT w FROM Warn w WHERE w.warnStatus = :status")
	List<Warn> findAllByWarnStatus(WarnStatus status);

	Warn findByWarnNo(Integer warnNo);

	// 유저별 받은 경고 (처리 완료)
	@Query("SELECT w FROM Warn w WHERE w.warnDefendantUser = :user AND w.warnStatus = 'APPROVED'")
	List<Warn> findByUser(User user);

}
