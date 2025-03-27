package kr.co.anabada.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.FavorService;

@Controller
@RequestMapping("/mypage/favor")
public class FavorController {
	@Autowired
	FavorService service;
	
	@GetMapping
	public String favorList() throws IOException {
//		@SessionAttribute(name = "loggedInUser", required = false) User user, Model model
//		int userNo = user.getUserNo();
//		List<ItemImage> favorItemList = service.selectMyFavor(userNo);
		
//		model.addAttribute("list", favorItemList);
		return "mypage/favorPage";
	}
	
	@GetMapping("/item")
	public String favorItemList() {
		return "mypage/favorItem";
	}
}
