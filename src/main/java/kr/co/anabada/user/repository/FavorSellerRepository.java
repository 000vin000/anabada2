package kr.co.anabada.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.user.entity.FavorSeller;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;

@Repository
public interface FavorSellerRepository extends JpaRepository<FavorSeller, Integer> {
	boolean countByUserAndSeller(User user, Seller seller);
}
