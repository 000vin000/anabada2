package kr.co.anabada.item.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.repository.ItemRepository;

@Service
public class ItemService {
	@Autowired
    private ItemRepository itemRepository;
	
	public Item findById(Integer itemNo) {
        Optional<Item> optionalItem = itemRepository.findById(itemNo);
        return optionalItem.orElse(null);  // 아이템이 없으면 null 반환
    }


	 public Item saveItem(Item item) {
		 return itemRepository.save(item);
	 }
}
