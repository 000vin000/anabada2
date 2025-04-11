package kr.co.anabada.admin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.admin.dto.ReportDTO;
import kr.co.anabada.admin.entity.Warn;
import kr.co.anabada.admin.entity.Warn.WarnReason;
import kr.co.anabada.admin.entity.Warn.WarnStatus;
import kr.co.anabada.admin.entity.Warn.WarnWhere;
import kr.co.anabada.admin.service.WarnService;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ItemService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.UserService;

@RestController
public class ReportUserRestController {
    @Autowired
    private JwtAuthHelper jwtAuthHelper;
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private WarnService warnService;
    
	@PostMapping("/report/submit")
	public ResponseEntity<?> reportSubmit(HttpServletRequest req, @RequestBody ReportDTO form) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
        
        System.out.println(form);
        
        if (form.getWarnDefendantUser() == null || form.getWarnDefendantUser().isBlank()) {
        	return ResponseEntity.badRequest().body(Map.of("error", "신고 대상 유저 정보가 없습니다."));
        }
        if (form.getWarnReason() == null || form.getWarnReason().isBlank()) {
        	return ResponseEntity.badRequest().body(Map.of("error", "warnReason X"));
        }

        if (form.getWarnWhere().contains("detail") && (form.getWarnItem() == null || form.getWarnItem().isBlank())) {
        	return ResponseEntity.badRequest().body(Map.of("error", "warnItem X"));
        }
        
        // 신고당한 유저
        User defendantUser = userService.getUser(Integer.valueOf(form.getWarnDefendantUser()));        
        
        // 신고 경로
        WarnWhere where;
        String warnWhere = form.getWarnWhere();
        
        Item whereItem = null;
        if (warnWhere.contains("profile")) {
            where = WarnWhere.PROFILE;
        } else if (warnWhere.contains("detail")) {
            where = WarnWhere.ITEM;
            whereItem = itemService.findById(Integer.valueOf(form.getWarnItem()));
        } else if (warnWhere.contains("chat")) {
            where = WarnWhere.CHATTING;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "잘못된 접근입니다."));
        }
        
        // 신고 사유
        WarnReason reason;
        
        String reasonDetail = null;
        switch (form.getWarnReason()) {
        case "SPAM":
            reason = WarnReason.SPAM;
            break;
        case "PORNOGRAPHY":
            reason = WarnReason.PORNOGRAPHY;
            break;
        case "ILLEGALITY":
            reason = WarnReason.ILLEGALITY;
            break;
        case "HARM":
            reason = WarnReason.HARM;
            break;
        case "ABUSE":
            reason = WarnReason.ABUSE;
            break;
        case "PRIVACY":
            reason = WarnReason.PRIVACY;
            break;
        case "OTHER":
            reason = WarnReason.OTHER;
            reasonDetail = form.getWarnReasonDetail();
            break;
        default:
            return ResponseEntity.badRequest().body(Map.of("error", "유효하지 않은 신고 사유입니다."));
    }
        
        Warn report = Warn.builder().warnPlaintiffUser(userService.getUser(user.getUserNo()))
        							.warnDefendantUser(defendantUser)
        							.warnWhere(where)
        							.warnItem(whereItem)
        							.warnReason(reason)
        							.warnReasonDetail(reasonDetail)
        							.warnStatus(WarnStatus.REQUESTED)
        							.build();
        
        warnService.insertWarn(report);
        		
        return ResponseEntity.ok(Map.of("message", "신고가 정상적으로 접수되었습니다."));
	}
}
