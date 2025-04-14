package kr.co.anabada.admin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import kr.co.anabada.user.service.UserService;

@RestController
public class ManagementRestController {
	@Autowired
	private JwtAuthHelper jwtAuthHelper;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private WarnService warnService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/warn/submitWarnResult/{warnNo}")
	public ResponseEntity<?> approveWarn(HttpServletRequest req, 
										@PathVariable String warnNo,
										@RequestBody WarnResultRequestDTO result) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "토큰 없음"));
        }
        Admin admin = adminService.findByUserNo(user.getUserNo());
        if (admin == null) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "관리자 권한 필요"));
        }
		
		boolean success = warnService.approveWarn(warnNo, result, admin); // 처리한 관리자 등록
		boolean plusWarnCnt = userService.plusWarnCtn(warnNo);
		
		
		if (success && plusWarnCnt) {
			return ResponseEntity.ok(Map.of("message", "처리 완료되었습니다."));
		} else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "오류 발생"));
		}
	}
	
	@DeleteMapping("/warn/deleteWarn/{warnNo}")
	public ResponseEntity<?> deleteWarn(HttpServletRequest req,	@PathVariable String warnNo) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "토큰 없음"));
        }
        Admin admin = adminService.findByUserNo(user.getUserNo());
        if (admin == null) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "관리자 권한 필요"));
        }
        
        boolean success = warnService.rejectWarn(warnNo, admin);
        
        if (success) {
			return ResponseEntity.ok(Map.of("message", "삭제 완료되었습니다."));
		} else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "오류 발생"));
		}
	}
} 
