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
import kr.co.anabada.coin.entity.ChangeCoin.ChangeCoinType;
import kr.co.anabada.coin.entity.Conversion;
import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.repository.ChangeCoinRepository;
import kr.co.anabada.coin.entity.Conversion.ConversionType;
import kr.co.anabada.coin.service.ChangeCoinService;
import kr.co.anabada.coin.service.ConversionService;
import kr.co.anabada.coin.service.GoodsService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
public class CashToCoinController {
	@Autowired
	JwtAuthHelper jwtAuthHelper;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	ConversionService conversionService;
	
	@Autowired
	ChangeCoinService changeCoinService;
	
	@PostMapping("/changeChargeToCoin")
	public ResponseEntity<?> submitChange(HttpServletRequest req, @RequestParam Integer changeCashToCoin) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		
		if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
		
		// 충전 신청 금액
		BigDecimal amount = BigDecimal.valueOf(changeCashToCoin);
		
		// 현재 현금 잔액에서 마이너스
		Goods current = goodsService.checkCurrentCashCoin(user.getUserNo());
		if (current.getGoodsCash().compareTo(amount) == -1) {
			return ResponseEntity.ok().body(new HashMap<String, String>() {{
				put("message", "전환할 금액이 보유 중인 잔액보다 적습니다.");
			}});
		}
		
		// 전환 신청 내역에 삽입
		conversionService.insertConversion(user.getUserNo(), ConversionType.TOCOIN, amount);
		
		amount = current.getGoodsCash().subtract(amount);
		Goods updateGoods = goodsService.updateGoodsCash(user.getUserNo(), amount);
		
		String successMessage = "전환 신청 되었습니다.";
		return ResponseEntity.ok().body(new HashMap<String, String>() {{
			put("message", successMessage);
		}});
	}

	@PostMapping("/changeChargeToCash")
	public ResponseEntity<?> submitChangeToCash(HttpServletRequest req, @RequestParam Integer changeCoinToCash) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		
		if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
		
		// 전환 신청 금액
		BigDecimal amount = BigDecimal.valueOf(changeCoinToCash);
		
		// 현재 코인 잔액에서 마이너스
		Goods current = goodsService.checkCurrentCashCoin(user.getUserNo());
		if (current.getGoodsCoin().compareTo(amount) == -1) {
			return ResponseEntity.ok().body(new HashMap<String, String>() {{
				put("message", "전환할 코인이 보유 중인 코인보다 적습니다.");
			}});
		}
		
		// 전환 신청 내역에 삽입
		conversionService.insertConversion(user.getUserNo(), ConversionType.TOCASH, amount);
		
		amount = current.getGoodsCoin().subtract(amount);
		Goods updateGoods = goodsService.updateGoodsCoin(user.getUserNo(), amount);
		
		String successMessage = "전환 신청 되었습니다.";
		return ResponseEntity.ok().body(new HashMap<String, String>() {{
			put("message", successMessage);
		}});
	}
}
