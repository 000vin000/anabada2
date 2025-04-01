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
import kr.co.anabada.admin.service.AcceptConversionService;
import kr.co.anabada.admin.service.AdminService;
import kr.co.anabada.coin.entity.Conversion;
import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.service.ConversionService;
import kr.co.anabada.coin.service.GoodsService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
@RequestMapping("/api/admin")
public class AcceptConversionRestController {
    @Autowired
    private JwtAuthHelper jwtAuthHelper;
    
    @Autowired
    private AcceptConversionService acptConService;
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private ConversionService conService;
    
    @GetMapping("/conversionList")
    public ResponseEntity<?> getConversionList(HttpServletRequest req) {
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
        
        List<Conversion> conList = acptConService.getConList();
        return ResponseEntity.ok(Map.of("conList", conList));
    }
    
    // 관리자 수락
    @PostMapping("/acceptConversion/{conversionNo}")
    public ResponseEntity<?> updateConversion(HttpServletRequest req, @PathVariable Integer conversionNo) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "관리자 로그인 필요"));
        }
        
        Admin admin = adminService.findByUserNo(user.getUserNo());
        conService.acceptConversion(conversionNo, admin);
        
        // conversionNo의 userNo로 goods 정보를 가져와 goodsCoin update
        adminService.updateCoin(conversionNo);
        
        return ResponseEntity.ok(Map.of("message", "전환 완료"));
    }
}
