package kr.co.anabada.coin.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.coin.dto.ChangeCoinDTO;
import kr.co.anabada.coin.entity.Account;
import kr.co.anabada.coin.entity.Conversion;
import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.service.AccountService;
import kr.co.anabada.coin.service.ChangeCoinService;
import kr.co.anabada.coin.service.ConversionService;
import kr.co.anabada.coin.service.GoodsService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
@RequestMapping("/api/coin")
public class CoinRestController {
    @Autowired
    private JwtAuthHelper jwtAuthHelper;
    
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ConversionService conversionService;
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private ChangeCoinService coinService;
    
    // 마이페이지에 현재 정보 추가
    @GetMapping
    public ResponseEntity<?> checkCurrentCashCoin(HttpServletRequest req) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }

        Goods goods = goodsService.checkCurrentCashCoin(user.getUserNo());
        if (goods == null) {
            goods = goodsService.insertUserGoods(user.getUserNo());
        }

        return ResponseEntity.ok(Map.of("goods", goods));
    }
    
    // 코인 충전창에 현재 정보 추가
    @GetMapping("/cashToCoin")
    public ResponseEntity<?> CurrentCashCoin(HttpServletRequest req) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
    	
    	if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
    	
    	Goods goods = goodsService.checkCurrentCashCoin(user.getUserNo());
    	
    	return ResponseEntity.ok(Map.of("goods", goods));
    }
    
    // 코인 전환 신청 내역
    @GetMapping("/conversionList")
    public ResponseEntity<?> getConversionList(HttpServletRequest req) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
    	if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
    	
    	List<Conversion> conversion = conversionService.toCoinfindByUserNo(user.getUserNo());
    	System.out.println(conversion);
    	
    	return ResponseEntity.ok(Map.of("conList", conversion));
    }
    
    // 코인 전환 신청 내역 삭제
    @DeleteMapping("cancelConversion/{conversionNo}")
    public ResponseEntity<?> deleteConversionList(HttpServletRequest req, @PathVariable Integer conversionNo) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
    	if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
    	
    	Optional<Conversion> history = conversionService.findByConversionNo(conversionNo);
    	BigDecimal conversionAmount = history.map(Conversion::getConversionAmount).orElse(BigDecimal.ZERO);

    	Goods goods = goodsService.checkCurrentCashCoin(user.getUserNo());
    	conversionAmount = conversionAmount.add(goods.getGoodsCash());
    	
    	goodsService.updateGoodsCash(user.getUserNo(), conversionAmount);
    	conversionService.deleteConversion(conversionNo);
    	
    	return ResponseEntity.ok(Map.of("message", "신청 취소 되었습니다."));
    }
    
    // 현금 충전 내역
    @GetMapping("/chargeList")
    public ResponseEntity<?> getChargeList(HttpServletRequest req) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
    	if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
    	
    	List<Account> chargeList = accountService.getChargeList(user.getUserNo());
    	System.out.println(chargeList);
    	
    	return ResponseEntity.ok(Map.of("chargeList", chargeList));
    }
    
    // 코인 사용 내역
    @GetMapping("/useCoinList")
    public ResponseEntity<?> getUseCoinList(HttpServletRequest req) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
    	if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
    	
    	List<ChangeCoinDTO> coinList = coinService.getUseList(user.getUserNo());
    	System.out.println(coinList);
    	
    	return ResponseEntity.ok(Map.of("coinList", coinList));
    }
    
    // 현금 전환 신청 내역
    @GetMapping("/conversionToCashList")
    public ResponseEntity<?> getConversionToCashList(HttpServletRequest req) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
    	if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
    	
    	List<Conversion> conversion = conversionService.toCashfindByUserNo(user.getUserNo());
    	System.out.println(conversion);
    	
    	return ResponseEntity.ok(Map.of("conList", conversion));
    }
    
    // 현금 전환 신청 내역 삭제
    @DeleteMapping("/cancelConversionToCash/{conversionNo}")
    public ResponseEntity<?> deleteConversionToCashList(HttpServletRequest req, @PathVariable Integer conversionNo) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
    	if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
    	
    	Optional<Conversion> history = conversionService.findByConversionNo(conversionNo);
    	BigDecimal conversionAmount = history.map(Conversion::getConversionAmount).orElse(BigDecimal.ZERO);

    	Goods goods = goodsService.checkCurrentCashCoin(user.getUserNo());
    	conversionAmount = conversionAmount.add(goods.getGoodsCoin());
    	
    	goodsService.updateGoodsCoin(user.getUserNo(), conversionAmount);
    	conversionService.deleteConversion(conversionNo);
    	
    	return ResponseEntity.ok(Map.of("message", "신청 취소 되었습니다."));
    }
}
