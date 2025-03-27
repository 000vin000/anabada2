package kr.co.anabada.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

import kr.co.anabada.coin.service.GoodsService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MypageController {
	@Autowired
    private JwtAuthHelper jwtAuthHelper;
	
	@Autowired
    private GoodsService goodsService;
	
	@GetMapping("/mypage")
    public String mypage(HttpServletRequest request, Model model) {
        // 요청 헤더에서 Authorization 값 가져오기
        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);
        
        if (authHeader == null) {
            log.warn("Authorization 헤더가 없거나 올바르지 않음");
            return "redirect:/auth/login.html"; 
        }

        // JWT 토큰 검증 및 사용자 정보 추출
        UserTokenInfo userInfo = jwtAuthHelper.getUserFromRequest(request);
        if (userInfo == null) {
            log.warn("유효하지 않은 토큰");
        }

        // 사용자 정보를 모델에 추가하여 뷰에서 활용 가능
        model.addAttribute("user", userInfo);

        return "mypage/mypage_main";
    }
}