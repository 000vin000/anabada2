package kr.co.anabada.coin.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.coin.entity.Account;
import kr.co.anabada.user.entity.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	@Query("SELECT a FROM Account a WHERE a.userNo = :userNo AND a.accountType = 'DEPOSIT'")
	List<Account> depositFindByUser(User userNo);
	
	@Query("SELECT a FROM Account a WHERE a.userNo = :userNo AND a.accountType = 'WITHDRAWAL'")
	List<Account> withdrawFindByUser(User userNo);

	// 관리자 수락
	@Modifying
	@Transactional
	@Query("UPDATE Account c SET c.adminNo = :adminNo, c.accountAt = :now WHERE c.accountNo = :accountNo")
	int updateAccount(Integer accountNo, Admin adminNo, LocalDateTime now);
}
