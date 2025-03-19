package kr.co.anabada.main.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.main.dto.ItemInclude1Image;
import kr.co.anabada.main.repository.ItemInclude1ImageRepository;

@Service
public class MainService {
	@Autowired 
	private ItemInclude1ImageRepository repo;
	
	public List<ItemInclude1Image> findAll() {
		return repo.findAllItems();
	}

	// 검색
	public List<ItemInclude1Image> findItems(String findType, String keyword) {
		return repo.findSearchItems(findType, keyword);
	}
	
	// 정렬
	@Transactional(readOnly = true)
    public List<ItemInclude1Image> sortByOrder(List<ItemInclude1Image> list, String sortOrder) {
    	LocalDateTime now = LocalDateTime.now();
    	
        switch (sortOrder) {
            case "new":
                Collections.sort(list, Comparator.comparing(ItemInclude1Image::getItem_sale_start_date));
                break;
            case "deadline":
                Collections.sort(list, Comparator.comparing(ItemInclude1Image::getItem_sale_end_date));
                break;
            case "popular":
                Collections.sort(list, Comparator.comparing(ItemInclude1Image::getItem_view_cnt).reversed());
                break;
            case "asc":
                Collections.sort(list, Comparator.comparing(ItemInclude1Image::getItem_price));
                break;
            case "desc":
                Collections.sort(list, Comparator.comparing(ItemInclude1Image::getItem_price).reversed());
                break;
            default:
                break;
        }
        return list;
    }
}
