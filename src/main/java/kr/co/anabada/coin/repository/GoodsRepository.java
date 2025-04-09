package kr.co.anabada.coin.repository;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.user.entity.User;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer> {
	Goods findByUser(User user);
	
	@Modifying
	@Query("UPDATE Goods g SET g.goodsCoin = g.goodsCoin + :coinAmount WHERE g.user.userNo = :userNo")
	int addCoinToUser(@Param("userNo") Integer userNo, @Param("coinAmount") BigDecimal coinAmount);
}
