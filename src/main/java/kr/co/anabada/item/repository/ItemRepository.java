package kr.co.anabada.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.anabada.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	
}
