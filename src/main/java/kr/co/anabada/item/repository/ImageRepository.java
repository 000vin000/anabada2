package kr.co.anabada.item.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Image;
import kr.co.anabada.item.entity.Item;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
	Optional<Image> findFirstByItemNo(Item item);

	List<Image> findByItemNo(Item item);
}
