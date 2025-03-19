package kr.co.anabada.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recent")
public class RecentController {
	
	@GetMapping
	public String recentAll() {
		
		return "mypage/itemRecent";
	}
}