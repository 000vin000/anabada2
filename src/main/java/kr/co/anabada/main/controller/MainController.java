package kr.co.anabada.main.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.ItemImage;
import kr.co.anabada.main.service.MainService;

@Controller
public class MainController {
    @Autowired
    private MainService mainService;

    @GetMapping("/")
    public String form(Model model) throws IOException {
    	List<ItemImage> itemImageList = returnItemList();
    	model.addAttribute("itemList", itemImageList);
    	
        return "main/mainForm";
    }

    @GetMapping(params = "sortOrder")
    public String sortByOrder(@RequestParam String sortOrder, Model model) throws IOException {
        List<ItemImage> imageList = returnItemList();
        
        List<ItemImage> sortedList = mainService.sortByOrder(imageList, sortOrder); 
        model.addAttribute("itemList", sortedList);
        return "main/mainForm";
    }    
    
    public List<ItemImage> returnItemList() throws IOException {
    	List<ItemImage> itemList = mainService.selectAll();
    	return mainService.includeImage(itemList);
    }
}
