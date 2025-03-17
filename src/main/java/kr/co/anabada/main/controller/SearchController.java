package kr.co.anabada.main.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.ItemImage;
import kr.co.anabada.main.service.MainService;
import kr.co.anabada.main.service.SearchService;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private MainService mainService;
    
    @Autowired
    private SearchService searchService;
    
    @GetMapping
    public String search(@RequestParam String findType, @RequestParam String keyword,
            				Model model) throws IOException {
        List<ItemImage> itemList = returnItemImage(findType, keyword);

        if (!itemList.isEmpty()) {
        	model.addAttribute("itemList", itemList);
        } else {
        	model.addAttribute("error", "검색 결과가 없습니다.");
        }
       
        return "main/searchForm";
    }
    
    // 정렬
    @GetMapping(params = "sortOrder")
    public String sortByOrder(@RequestParam String findType, @RequestParam String keyword, @RequestParam String sortOrder, Model model) throws IOException {
    	List<ItemImage> itemList = returnItemImage(findType, keyword);
        
        List<ItemImage> sortedList = mainService.sortByOrder(itemList, sortOrder);
        model.addAttribute("itemList", sortedList);

        return "main/searchForm";
    }
    
    public List<ItemImage> returnItemImage(String findType, String keyword) throws IOException {
    	if (!(findType.equals("itemName") || findType.equals("userNick"))) {
    		throw new RuntimeException();  // 500
    	} 
    	
    	List<ItemImage> itemList = searchService.searchItems(findType, keyword);
    	return mainService.includeImage(itemList);
    }
}
