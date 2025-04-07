package kr.co.anabada.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.coin.entity.Account;

@Repository
public interface DepositWithdrawalRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a WHERE a.adminNo IS NULL AND a.accountAt IS NULL")
    List<Account> getAccountList();
}

