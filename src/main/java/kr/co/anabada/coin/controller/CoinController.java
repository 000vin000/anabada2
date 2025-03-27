package kr.co.anabada.coin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.anabada.jwt.JwtAuthHelper;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CoinController {
    @Autowired
    private JwtAuthHelper jwtAuthHelper;

    // 금액 충전 창
    @GetMapping("/chargeCash")
    public String chargeCash() {
        return "mypage/chargeCash";
    }
    
    /*
    // 충전
    @PostMapping("/submitCharge")
    public ResponseEntity<?> submitCharge(HttpServletRequest request,
            @RequestParam("chargetype") String chargeTypeStr, // 문자열로 받기
            @RequestParam(value = "depositorName", required = false) String depositorName,
            @RequestParam(value = "amount", required = false) BigDecimal amount,
            @RequestParam(value = "cardNumber", required = false) String cardNumber,
            @RequestParam(value = "cardAmount", required = false) BigDecimal cardAmount) { 

        // 문자열을 PayType으로 변환 (+ 예외 처리)
        PayType chargeType;
        try {
            chargeType = PayType.valueOf(chargeTypeStr.toUpperCase()); // 대소문자 무시 변환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("잘못된 결제 유형: " + chargeTypeStr);
        }
        
        // 사용자 인증
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(request);        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        // 결제 금액 설정
        BigDecimal chargeAmount = (chargeType == PayType.NOPASSBOOK) ? amount : cardAmount;
        if (chargeAmount == null || chargeAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("유효한 금액을 입력해주세요.");
        }

        // 현재 현금 잔액 조회
        BigDecimal userCurrentBalance = accountService.getCurrentCash(user.getUserNo());
        BigDecimal userNewBalance = userCurrentBalance.add(chargeAmount);
        // 충전 정보 생성
        Account account = Account.builder()
                .accountType(Account.AccountType.DEPOSIT)
                .accountAmount(chargeAmount)
                .accountBalance(userNewBalance)
                .accountPayType(chargeType)
                .build();

        // 충전 (DB에 기록 남기기)
        try {
            accountService.insertAccount(account);
            log.info("충전 완료: {}원", chargeAmount);
            
            // int result = goodsService.updateBalance(user.getUserNo(), userNewBalance);
            // log.info("보유 금액 업데이트: {}", result);
        } catch (Exception e) {
            log.error("충전 처리 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("충전 처리에 실패했습니다. 잠시 후 다시 시도해주세요.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("충전 완료: " + chargeAmount + "원");
    }
    */
}
