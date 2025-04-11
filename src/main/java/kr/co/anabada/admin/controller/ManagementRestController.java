package kr.co.anabada.admin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.admin.dto.WarnResultRequestDTO;
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.service.AdminService;
import kr.co.anabada.admin.service.WarnService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
public class ManagementRestController {
	@Autowired
	private JwtAuthHelper jwtAuthHelper;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private WarnService warnService;
	
	@PostMapping("/warn/submitWarnResult/{warnNo}")
	public ResponseEntity<?> approveWarn(HttpServletRequest req, 
										@PathVariable String warnNo,
										@RequestBody WarnResultRequestDTO result) {
		// admin 확인
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "토큰 없음"));
        }
        Admin admin = adminService.findByUserNo(user.getUserNo());
        System.out.println("Admin : " + admin);
        if (admin == null) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "관리자 권한 필요"));
        }
		
		boolean success = warnService.approveWarn(warnNo, result, admin);
		System.out.println("success : " + success);
		
		if (success) {
			return ResponseEntity.ok(Map.of("message", "처리 완료되었습니다."));
		} else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "오류 발생"));
		}
	}
} 
