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
import kr.co.anabada.coin.entity.Account.PayType;
import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.service.AccountService;
import kr.co.anabada.coin.service.GoodsService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
public class CashChargeController {
	@Autowired
	JwtAuthHelper jwtAuthHelper;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	GoodsService goodsService;
	
	@PostMapping("/submitCharge")
	public ResponseEntity<?> submitCharge(HttpServletRequest req, @RequestParam String chargetype,
	                                       @RequestParam(required = false) String depositorName,
	                                       @RequestParam(required = false) Integer amount,
	                                       @RequestParam(required = false) String cardNumber,
	                                       @RequestParam(required = false) Integer cardAmount) {
	    UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);

	    if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
	    
	    PayType payType = PayType.valueOf(chargetype.toUpperCase()); 
	    BigDecimal insertAmount;
	    if (payType == PayType.NOPASSBOOK) insertAmount = BigDecimal.valueOf(amount);
	    else insertAmount = BigDecimal.valueOf(cardAmount);

	    // DB에 기록
	    Account account = accountService.insertAccountDeposit(user.getUserNo(), payType, insertAmount);

	    // 충전 완료 메시지를 객체 형태로 리턴
	    String successMessage = insertAmount + "원 입금 요청";
	    return ResponseEntity.ok().body(new HashMap<String, String>() {{
	        put("message", successMessage);
	    }});
	}

}
