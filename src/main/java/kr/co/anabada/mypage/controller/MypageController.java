package kr.co.anabada.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MypageController {
	@Autowired
    private JwtAuthHelper jwtAuthHelper;
	
	@GetMapping("/mypage")
    public String mypage(HttpServletRequest request, Model model) {
        // 요청 헤더에서 Authorization 값 가져오기
		UserTokenInfo userInfo = jwtAuthHelper.getUserFromRequest(request);
		// System.out.println(userInfo.getNickname());
		// 
		// model.addAttribute("user", userInfo);

        return "mypage/mypage_main";
    }
}