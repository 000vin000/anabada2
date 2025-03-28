package kr.co.anabada.item.controller;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.item.dto.ItemDetailDTO;
import kr.co.anabada.item.service.ItemDetailService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;
import kr.co.anabada.user.service.UserService;

@Controller
@RequestMapping("/item/detail/{itemNo}")
public class ItemDetailController {
	@Autowired
	private ItemDetailService itemDetailService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtAuthHelper jwtAuthHelper;
	
	@GetMapping
	public String getItemDetail(@PathVariable Integer itemNo, Model model, HttpServletRequest req) throws NotFoundException {
		UserTokenInfo userTokenInfo = jwtAuthHelper.getUserFromRequest(req);
		Integer userNo = (userTokenInfo != null) ? userTokenInfo.getUserNo() : null;
		ItemDetailDTO item = itemDetailService.getItemDetailDTO(itemNo, userNo);
		model.addAttribute("item", item);
		model.addAttribute("user", userTokenInfo);
		return "item/itemDetail";
	}
	
	private Long calculateRemainTime(LocalDateTime itemDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, itemDate);
        return duration.getSeconds();
    }
	
	@GetMapping("/remainTime/{type}")
	@ResponseBody
	public Long getRemainTime(@PathVariable Integer itemNo, @PathVariable String type) {
		switch(type) {
		case "start":
			return calculateRemainTime(itemDetailService.getSaleStartDate(itemNo));
		case "end":
			return calculateRemainTime(itemDetailService.getSaleEndDate(itemNo));
		}
		return null;
	}
	
	@GetMapping("/price")
	@ResponseBody
    public BigDecimal getPrice(@PathVariable Integer itemNo) {
        return itemDetailService.getPrice(itemNo);
    }
	
	@GetMapping("/status")
	@ResponseBody
	public String getStatus(@PathVariable Integer itemNo) {
		return itemDetailService.getStatus(itemNo);
	}
	
	@PatchMapping("/bid")
	@ResponseBody
	public ResponseEntity<String> updatePrice(
			@PathVariable Integer itemNo, @RequestBody Map<String, Long> request, HttpServletRequest req) throws NotFoundException {
		UserTokenInfo userTokenInfo = jwtAuthHelper.getUserFromRequest(req);
	    if (userTokenInfo == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요한 서비스입니다.");
	    }
	    
	    BigDecimal newPrice = BigDecimal.valueOf(request.get("newPrice"));
	    if (newPrice == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입찰가를 입력하세요.");
	    }

	    int userNo = userTokenInfo.getUserNo();
		ItemDetailDTO itemDetailDTO = itemDetailService.getItemDetailDTO(itemNo, userNo);
	    int sellerNo = itemDetailDTO.getSellerNo();
	    if (userNo == sellerNo) {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("자신의 물품은 입찰할 수 없습니다.");
	    }
	    
	    User user = userRepository.findById(userNo)
	            .orElseThrow(() -> new EntityNotFoundException("유저 정보를 불러올 수 없습니다."));
	    if (itemDetailService.updatePrice(itemNo, newPrice, user)) {
	        return ResponseEntity.ok("입찰 완료되었습니다.");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입찰가는 현재가보다 1,000원 이상 높아야 합니다.");
	    }
	}
}
