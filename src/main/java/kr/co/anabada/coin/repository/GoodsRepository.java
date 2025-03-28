package kr.co.anabada.coin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.user.entity.User;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer> {
	Goods findByUser(User user);
}
