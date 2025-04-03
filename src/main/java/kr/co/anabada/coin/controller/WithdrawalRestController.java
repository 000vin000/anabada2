package kr.co.anabada.coin.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.coin.entity.Account;
import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.service.AccountService;
import kr.co.anabada.coin.service.GoodsService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
public class WithdrawalRestController {
    @Autowired
    private JwtAuthHelper jwtAuthHelper;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private GoodsService goodsService;
    
	@PostMapping("/withdrawalCash")
	public ResponseEntity<?> withdrawalCash(HttpServletRequest req, @RequestParam Integer withdrawalCash,
											@RequestParam String withdrawalBank,
											@RequestParam String withdrawalAccount) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
        
        // 출금 신청 금액 type => BigDecimal
        BigDecimal amount = BigDecimal.valueOf(withdrawalCash);
        
		// goods 테이블 업데이트
        Goods current = goodsService.checkCurrentCashCoin(user.getUserNo());
        if (current.getGoodsCash().compareTo(amount) == -1) {
			return ResponseEntity.ok().body(new HashMap<String, String>() {{
				put("message", "신청한 금액이 보유 중인 금액보다 적습니다.");
			}});
		}
        BigDecimal cash = current.getGoodsCash().subtract(amount);
        Goods goods = goodsService.updateGoodsCash(user.getUserNo(), cash);
        
		// 출금 신청 DB에 등록
        Account account = accountService.insertAccountWithdrawal(user.getUserNo(), amount, withdrawalBank, withdrawalAccount);
		
		return ResponseEntity.ok(Map.of("message", "출금 신청 완료"));
	}
}
