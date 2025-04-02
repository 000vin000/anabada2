package kr.co.anabada.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

	int countByItemItemNo(Integer itemNo); //userProfile
	
}
