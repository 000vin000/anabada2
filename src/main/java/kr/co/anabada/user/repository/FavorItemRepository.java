package kr.co.anabada.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.FavorItem;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;

@Repository
public interface FavorItemRepository extends JpaRepository<FavorItem, Integer> {
	boolean countByUserAndItem(User user, Item item);
}
