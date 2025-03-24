package kr.co.anabada.main.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.main.GenderNotFoundException;
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
		else throw new GenderNotFoundException("Gender not found for: " + gender);
		return itemList;
	}

	// 이미지 파일을 Base64로 인코딩
	public void encodeImageFile(List<ItemInclude1Image> itemList) throws IOException {
	    for (ItemInclude1Image item : itemList) {
	        byte[] imageBytes = item.getImage_file();  // image_file을 byte[]로 처리
	        if (imageBytes != null) {
	            item.setBase64Image(Base64.getEncoder().encodeToString(imageBytes));
	        }
	    }
	}
}
