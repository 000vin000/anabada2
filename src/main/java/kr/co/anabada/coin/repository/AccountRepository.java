package kr.co.anabada.coin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.coin.entity.Account;
import kr.co.anabada.user.entity.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	@Query("SELECT a FROM Account a WHERE a.userNo = :userNo AND a.accountType = 'DEPOSIT'")
	List<Account> depositFindByUser(User userNo);
}
