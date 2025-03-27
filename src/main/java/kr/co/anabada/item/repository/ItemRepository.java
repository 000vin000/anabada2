package kr.co.anabada.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.Seller;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findBySeller(Seller seller);

	int countBySeller_SellerNo(Integer sellerNo); //userProfile
}
