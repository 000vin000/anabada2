package kr.co.anabada.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController {
	@GetMapping("/mypage")
	public String mypage() {
		return "mypage/mypage_main";
	}
}
