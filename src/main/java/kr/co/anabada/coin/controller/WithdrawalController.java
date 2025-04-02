package kr.co.anabada.coin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WithdrawalController {
	@GetMapping("/withdrawal")
	public String withdrawal() {
		return "mypage/withdrawal";
	}
}
