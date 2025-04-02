package kr.co.anabada.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Item_Category;

@Repository
public interface ItemCategoryRepository extends JpaRepository<Item_Category, String> {
	
}
