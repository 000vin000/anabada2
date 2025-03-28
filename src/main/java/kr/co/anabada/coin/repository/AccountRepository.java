package kr.co.anabada.coin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.coin.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	
}
