package kr.co.anabada.mypage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.co.anabada.admin.entity.Warn;
import kr.co.anabada.admin.service.WarnService;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.UserService;

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
	
	@GetMapping("/toCoin")
	public String toCoin() {
		return "mypage/toCoin";
	}
	
	@GetMapping("/toCash")
	public String toCash() {
		return "mypage/toCash";
	}
	
	@GetMapping("/myWarnList") 
	public String myWarnList() {
		return "mypage/myWarnList";
	}
}