package kr.co.anabada.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.anabada.main.dto.ItemInclude1Image;
import kr.co.anabada.main.service.MainService;

@Controller
public class MainController {
	@Autowired
	private MainService service;
	
	@GetMapping("/")
	public String getAllItems(Model model) {
		List<ItemInclude1Image> itemList = service.findAll();
    	model.addAttribute("itemList", itemList);
    	
        return "main/mainForm";
	}
}
