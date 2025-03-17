package kr.co.anabada.mypage.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.anabada.item.entity.ItemImage;
import kr.co.anabada.mypage.service.FavorService;
import kr.co.anabada.user.entity.User;

@Controller
@RequestMapping("/mypage/itemfavor")
public class FavorController {
	@Autowired
	FavorService service;
	
	@GetMapping
	public String favorList(@SessionAttribute(name = "loggedInUser", required = false) User user, Model model) throws IOException {
		int userNo = user.getUserNo();
		List<ItemImage> favorItemList = service.selectMyFavor(userNo);
		
		model.addAttribute("list", favorItemList);
		return "mypage/itemfavor";
	}
}
