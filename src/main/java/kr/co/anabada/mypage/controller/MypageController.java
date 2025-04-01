package kr.co.anabada.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.anabada.coin.service.GoodsService;

@Controller
public class MypageController {
	@Autowired
	private GoodsService goodsService;
	
	@GetMapping("/mypage")
    public String mypage() {
        return "mypage/mypage_main";
    }
	
	@GetMapping("/chargeCash")
	public String chargeCash() {
		return "mypage/chargeCash";
	}
	
	@GetMapping("/toCoin")
	public String toCoin() {
		return "mypage/toCoin";
	}
}