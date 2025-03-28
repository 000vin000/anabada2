package kr.co.anabada.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.user.entity.Seller;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findBySeller(Seller seller);

	int countBySeller_SellerNo(Integer sellerNo); // userProfile

	@Query("SELECT itemNo FROM Item")
	List<Integer> findAllItemNos(); // ItemStatusScheduler

	@Modifying
	@Transactional
	@Query("UPDATE Item SET itemStatus = ItemStatus.ACTIVE " +
			"WHERE itemStatus = ItemStatus.WAITING " +
			"AND itemSaleStartDate <= now()")
	int activateWaitingItems(); //ItemStatusScheduler

	@Modifying
	@Transactional
	@Query("UPDATE Item i SET i.itemStatus = ItemStatus.EXPIRED " +
			"WHERE i.itemStatus = ItemStatus.ACTIVE " +
			"AND i.itemSaleEndDate <= now() " +
			"AND NOT EXISTS (SELECT b FROM Bid b WHERE b.item = i)")
	int expireActiveItems(); //ItemStatusScheduler
	
	@Modifying
	@Transactional
	@Query("UPDATE Item i SET i.itemStatus = ItemStatus.RESERVED " +
			"WHERE i.itemStatus = ItemStatus.ACTIVE " +
			"AND i.itemSaleEndDate <= now() " +
			"AND EXISTS (SELECT b FROM Bid b WHERE b.item = i)")
	int reserveActiveItems(); //ItemStatusScheduler
}
