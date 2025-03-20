package kr.co.anabada.main.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.anabada.main.dto.ItemInclude1Image;
import kr.co.anabada.main.service.MainService;

@Controller
public class MainController {
	@Autowired
	private MainService service;
	
	@GetMapping("/")
	public String getAllItems(@RequestParam(required = false) String sortOrder, Model model) throws IOException {
		List<ItemInclude1Image> itemList = service.findAll();
		encodeImageFile(itemList);
		
		itemList = sort(sortOrder, itemList);
		
    	model.addAttribute("itemList", itemList);
    	
        return "main/mainForm";
	}

	@GetMapping("/search")
	public String search(@RequestParam String findType, @RequestParam String keyword, 
	                     @RequestParam(required = false) String sortOrder, Model model) throws IOException {
		List<ItemInclude1Image> itemList = service.findItems(findType, keyword);
		encodeImageFile(itemList);
		
		// 검색 결과가 있을 경우
		if (itemList != null || !itemList.isEmpty()) {
			// sortOrder가 있을 경우 정렬을 수행
			itemList = sort(sortOrder, itemList); 
			model.addAttribute("itemList", itemList);
		} else {
			model.addAttribute("error", "검색 결과가 없습니다.");
		}
		return "main/searchForm";
	}

	@GetMapping("/category/{gender}")
	public String category(@PathVariable String gender, @RequestParam String ct,
							@RequestParam String cd, @RequestParam(required = false) String sortOrder, Model model) throws IOException {
		List<ItemInclude1Image> itemList = service.findByCategory(gender, ct, cd);
		encodeImageFile(itemList);
		
		model.addAttribute("gender", gender);
		model.addAttribute("ct", ct);
		model.addAttribute("cd", cd);
		
		if (itemList != null && !itemList.isEmpty()) {
			// sortOrder가 있을 경우 정렬을 수행
			itemList = sort(sortOrder, itemList); 
			model.addAttribute("itemList", itemList);
		} else {
			model.addAttribute("error", "검색 결과가 없습니다.");
		}
		
		return "main/cateForm";
	}
	
	// 정렬
	private List<ItemInclude1Image> sort(String sortOrder, List<ItemInclude1Image> itemList) {
		if (sortOrder != null && !sortOrder.isEmpty()) {
			itemList = service.sortByOrder(itemList, sortOrder);
		}
		return itemList;
	}
	
	// 이미지 파일을 Base64로 인코딩
	private void encodeImageFile(List<ItemInclude1Image> itemList) throws IOException {
	    for (ItemInclude1Image item : itemList) {
	        byte[] imageBytes = item.getImage_file();  // image_file을 byte[]로 처리
	        if (imageBytes != null) {
	            item.setBase64Image(Base64.getEncoder().encodeToString(imageBytes));
	        }
	    }
	}
}
