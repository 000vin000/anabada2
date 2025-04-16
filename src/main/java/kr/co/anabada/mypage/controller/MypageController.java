package kr.co.anabada.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@Controller
public class MypageController {	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@GetMapping("/mypage")
    public String mypage(@CookieValue(name = "Token", required = false) String token, Model model) {
        UserTokenInfo userinfo = jwtTokenHelper.extractUserInfoFromAccessToken(token);
        model.addAttribute("user", userinfo); // 쿠키에서 토큰을 불러와 속성에 유저 정보를 담아서 마이페이지로 이동 : jhu
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