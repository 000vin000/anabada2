package kr.co.anabada.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {
	Seller findByUser(User user);
}
