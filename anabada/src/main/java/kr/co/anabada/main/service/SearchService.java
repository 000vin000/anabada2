package kr.co.anabada.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.ItemImage;
import kr.co.anabada.main.mapper.SearchMapper;

@Service
public class SearchService {
	@Autowired
	private SearchMapper mapper;
	
    // 검색
	public List<ItemImage
	> searchItems(String findType, String keyword) {
		if (findType.equals("itemName")) {	// 상품명 검색
			return mapper.selectByItemName(keyword);
		} else if (findType.equals("userNick")) { // 닉네임 검색
			return mapper.selectByUserName(keyword);
		}
		return null;
	}
}
