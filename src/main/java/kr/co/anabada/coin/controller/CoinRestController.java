package kr.co.anabada.coin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.service.GoodsService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
@RequestMapping("/api/coin")
public class CoinRestController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private JwtAuthHelper jwtAuthHelper;

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
}
