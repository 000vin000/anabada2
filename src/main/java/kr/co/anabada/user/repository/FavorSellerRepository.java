package kr.co.anabada.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.user.entity.FavorSeller;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;

@Repository
public interface FavorSellerRepository extends JpaRepository<FavorSeller, Integer> {
	List<FavorSeller> findByUserOrderByFavorCreatedDateDesc(User user);
	FavorSeller findByUserAndSeller(User user, Seller seller);
	int countByUserAndSeller(User user, Seller seller);
	void deleteByUserAndSeller(User user, Seller seller);
}
