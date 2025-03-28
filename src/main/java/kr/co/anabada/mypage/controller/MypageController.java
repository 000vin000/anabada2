package kr.co.anabada.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController {
	@GetMapping("/mypage")
    public String mypage() {
        return "mypage/mypage_main";
    }
	
	@GetMapping("/chargeCash")
	public String chargeCash() {
		return "mypage/chargeCash";
	}
}