package kr.co.anabada.item.controller;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.item.dto.ItemDetailDTO;
import kr.co.anabada.item.service.ItemDetailService;

@Controller
@RequestMapping("/item/detail/{itemNo}")
public class ItemDetailController {
	@Autowired
	private ItemDetailService itemDetailService;
	
	@GetMapping
	public String getItemDetail(@PathVariable Integer itemNo, Model model) throws NotFoundException {
		ItemDetailDTO item = itemDetailService.getItemDetailDTO(itemNo);
		model.addAttribute("item", item);
		return "item/itemDetail";
	}
}
