package kr.co.anabada.main.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.ItemImage;
import kr.co.anabada.main.service.CategoryService;
import kr.co.anabada.main.service.MainService;

@Controller
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	private CategoryService cateService;
	
	@Autowired
	private MainService mainService;
	
	@GetMapping
	public String searchCategory(@RequestParam String gender, @RequestParam String clothesType,
									Model model) throws IOException {
		List<ItemImage> itemList = returnItemImage(gender, clothesType);
		
		if (!itemList.isEmpty()) {
			model.addAttribute("itemList", itemList);
		} else {
			model.addAttribute("error", "검색 결과가 없습니다.");
		}
		
		return "main/cateForm";
	}
	
	// 정렬
	@GetMapping(params = "sortOrder")
	public String sortByOrder(@RequestParam String gender, @RequestParam String clothesType, 
									@RequestParam String sortOrder, Model model) throws IOException {
		List<ItemImage> itemList = returnItemImage(gender, clothesType);
		
		List<ItemImage> sortedList = mainService.sortByOrder(itemList, sortOrder);
		model.addAttribute("itemList", sortedList);
		
		return "main/cateForm";
	}
	
	public List<ItemImage> returnItemImage(String gender, String clothesType) throws IOException {
		if (!(gender.equals("m") || gender.equals("w") || gender.equals(""))) {
		    throw new RuntimeException();
		}

		if (!(clothesType.equals("top") || clothesType.equals("bottom") || 
		      clothesType.equals("outer") || clothesType.equals("dress") || 
		      clothesType.equals("etc") || clothesType.equals("set") || clothesType.equals(""))) {
			throw new RuntimeException();
		}
		
		List<ItemImage> item = null;
		
		if (gender == "" && clothesType == "") {
			item = mainService.selectAll();
		} else if (gender.equals("")) {
			item = cateService.searchClothesType(clothesType);
		} else if (clothesType.equals("")) {
			item = cateService.searchGender(gender);
		} else {
			item = cateService.searchItems(gender, clothesType);
		}
			
		return mainService.includeImage(item);
	}
}
