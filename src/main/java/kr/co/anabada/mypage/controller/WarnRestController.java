package kr.co.anabada.mypage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.admin.entity.Warn;
import kr.co.anabada.admin.service.WarnService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.UserService;

@RestController
public class WarnRestController {
	@Autowired
    private JwtAuthHelper jwtAuthHelper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WarnService warnService;
	
	@GetMapping("/api/myWarnList") 
	public ResponseEntity<?> getMyWarnList(HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "토큰 없음"));
        }
        
        User myUserData = userService.getUser(user.getUserNo());
        System.out.println(myUserData);
        
        List<Warn> warns = warnService.getMyWarns(myUserData);
        if (warns == null || warns.isEmpty()) {
        	return ResponseEntity.ok(Map.of("error", "받은 경고가 없습니다."));
        }
        
        return ResponseEntity.ok(Map.of("warns", warns));
	}
}
