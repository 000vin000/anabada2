package kr.co.anabada.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
	Item findByItemNo(Integer itemNo);
}
