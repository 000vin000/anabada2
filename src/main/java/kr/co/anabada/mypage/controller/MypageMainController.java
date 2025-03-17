package kr.co.anabada.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageMainController {
	
	
	@GetMapping()
	public String mypageMain() {
		// TODO 구매현황 판매현황을 불러와서 페이지에 현황을 로드
		
		return "mypage/mypageMain";
	}
}
