package kr.co.anabada.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.service.AdminService;
import kr.co.anabada.admin.service.DepositWithdrawalService;
import kr.co.anabada.coin.entity.Account;
import kr.co.anabada.coin.service.AccountService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
@RequestMapping("/api/cash")
public class DepositWithdrawalRestController {
	@Autowired
	private JwtAuthHelper jwtAuthHelper;
	
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private DepositWithdrawalService dewiService;
    
    @Autowired
    private AccountService accountService;
	
	@GetMapping("/depositWithdrawalList")
	public ResponseEntity<?> getAccountList(HttpServletRequest req) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "관리자 로그인 필요"));
        }
        
        // 관리자 확인
        Admin admin = adminService.findByUserNo(user.getUserNo());
        if (admin == null || Boolean.TRUE.equals(admin.getCanManageFinances()) == false) {
        	return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("error", "관리자 권한 필요"));
        }
        
        List<Account> accountList = dewiService.getAccountList();
        return ResponseEntity.ok(Map.of("accountList", accountList));
	}
	
	// 관리자 확인
	@PostMapping("/acceptAccount/{accountNo}")
	public ResponseEntity<?> updateAccount(HttpServletRequest req, @PathVariable Integer accountNo) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "관리자 로그인 필요"));
        }
        
        Admin admin = adminService.findByUserNo(user.getUserNo());
        accountService.acceptAccount(accountNo, admin); 
        
        // accountNo의 userNo로 goods 정보를 가져와 goodsCash update (입금일 때만)
        adminService.updateCash(accountNo);

        return ResponseEntity.ok(Map.of("message", "입출금 확인"));
	}
}
