package kr.co.anabada.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.repository.BidRepository;
import kr.co.anabada.item.repository.ItemRepository;

@Service
public class BidListService {
	@Autowired
	private BidRepository bidRepo;
	
	@Autowired
	private ItemRepository itemRepo;
	
	public Item findById(Integer itemNo) {
        return itemRepo.findById(itemNo).orElseThrow(() -> new IllegalArgumentException("아이템 없음"));
    }
	
	public List<Bid> getBidListByItemNo(Integer itemNo) {
		Item item = findById(itemNo);
		return bidRepo.findByItem(item);
	}
}
