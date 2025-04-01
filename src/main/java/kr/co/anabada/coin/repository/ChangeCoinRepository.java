package kr.co.anabada.coin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.coin.entity.ChangeCoin;
import kr.co.anabada.user.entity.User;

@Repository
public interface ChangeCoinRepository  extends JpaRepository<ChangeCoin, Integer>  {
    // 사용자별 코인 변동 내역 받아오기
    @Query("SELECT c FROM ChangeCoin c WHERE c.userNo = :userNo")
    List<ChangeCoin> findByUser(User userNo);
}
