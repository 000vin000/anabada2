package kr.co.anabada.mypage.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.anabada.mypage.dto.BuylistDto;
import kr.co.anabada.mypage.service.BuyService;
import kr.co.anabada.user.entity.User;

@Controller
@RequestMapping("/mypage/itembuy")
public class BuyController {
	@Autowired
	BuyService service;
	
	@GetMapping
	public String getBuyList(@SessionAttribute(name = "loggedInUser", required = false) User user, Model model) throws IOException {
		int userNo = user.getUserNo();
		List<BuylistDto> buylist = service.selectAllBuy(userNo);
		
		model.addAttribute("buylist", buylist);
		return "mypage/itembuy";
	}
}
