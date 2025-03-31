package kr.co.anabada.buy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.buy.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
}
