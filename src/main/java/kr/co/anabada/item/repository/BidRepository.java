package kr.co.anabada.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.entity.Item;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

	int countByItemItemNo(Integer itemNo); //userProfile

	List<Bid> findByItem(Item itemNo); // bidList
	
}
