package kr.co.anabada.main.service;

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
	
	public List<ItemInclude1Image> sortByOrder(List<ItemInclude1Image> list, String sortOrder) {
	    switch (sortOrder) {
	        case "new":
	            list.sort(Comparator.comparing(ItemInclude1Image::getItem_sale_start_date));
	            break;
	        case "deadline":
	            list.sort(Comparator.comparing(ItemInclude1Image::getItem_sale_end_date));
	            break;
	        case "popular":
	            list.sort(Comparator.comparing(ItemInclude1Image::getItem_view_cnt).reversed());
	            break;
	        case "asc":
	            list.sort(Comparator.comparing(ItemInclude1Image::getItem_price));
	            break;
	        case "desc":
	            list.sort(Comparator.comparing(ItemInclude1Image::getItem_price).reversed());
	            break;
	        default:
	            break;
	    }
	    return list;
	}


	// 카테고리 검색
	public List<ItemInclude1Image> findByCategory(String gender, String ct, String cd) {
		List<ItemInclude1Image> itemList = null;

		String regex = "^sel\\d{2}$";
		if (cd.matches(regex)) cd = cd.substring(3);
		
		if (gender.equals("00")) itemList = repo.selectGenderAll(ct, cd);
		else if (gender.equals("10") || gender.equals("20")) itemList = repo.selectGender(gender, ct, cd);
		
		return itemList;
	}

	public String matchGender(String gender) {
		if (gender.equals("00")) return "전체";
		else if (gender.equals("10")) return "남성";
		else return "여성";
	}

	public String matchClothesType(String ct) {
		if (ct.equals("01")) return "아우터";
		else if (ct.equals("02")) return "상의";
		else if (ct.equals("03")) return "하의";
		else if (ct.equals("04")) return "원피스";
		else if (ct.equals("05")) return "스커트";
		else if (ct.equals("06")) return "가방";
		else if (ct.equals("07")) return "패션소품";
		else return "신발";
	}
}
