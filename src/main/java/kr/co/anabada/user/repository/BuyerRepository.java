package kr.co.anabada.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.user.entity.Buyer;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer>{
	Optional<Buyer> findByUser_UserNo(Integer userNo); //userProfile

	@Query("SELECT buyerNo FROM Buyer")
	List<Integer> findAllBuyerNos(); //userProfile
}
