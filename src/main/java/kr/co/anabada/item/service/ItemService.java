package kr.co.anabada.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.mapper.ItemMapper;

@Service
public class ItemService {
	@Autowired
	private ItemMapper itemMapper;
	
	public void save(Item item) {
        itemMapper.save(item);
    }
	
	 public List<Item> findItemsByUserNo(int userNo) {
	       return itemMapper.findItemsByUserNo(userNo);  // userNo를 기준으로 아이템을 조회
	    }
	 
	 public void updateItem(Item item) {
		 itemMapper.updateItem(item);
	 }
	 
	 public Item findItemById(int itemNo) {
		 return itemMapper.findItemById(itemNo);
	 }
	 
	 public void deleteItem(int itemNo) {
	        itemMapper.deleteItem(itemNo);
	    }
}
