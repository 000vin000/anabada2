package kr.co.anabada.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Image;
import kr.co.anabada.item.entity.Item;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
	Image findFirstByItemNo(Item item);
}
