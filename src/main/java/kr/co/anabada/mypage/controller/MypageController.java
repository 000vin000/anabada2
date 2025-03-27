package kr.co.anabada.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@Controller
public class MypageController {
	@GetMapping("/mypage")
	public String mypage() {
		return "mypage/mypage_main";
	}
}
