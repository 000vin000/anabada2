package kr.co.anabada.item.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.anabada.item.dto.ItemDetailDTO;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ItemDetailService;
import kr.co.anabada.user.entity.User;

@Controller
@RequestMapping("/item/detail/{itemNo}")
public class ItemDetailController {
	@Autowired
	private ItemDetailService itemDetailService;
	
	@GetMapping
	public String getItemDetail(@PathVariable Integer itemNo, Model model,
			@SessionAttribute(name = "loggedInUser", required = false) User user) throws NotFoundException {
		ItemDetailDTO item = itemDetailService.getItemDetailDTO(itemNo, user);
		model.addAttribute("item", item);
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
    public Long getPrice(@PathVariable Integer itemNo) {
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
			@PathVariable Integer itemNo, @RequestBody Map<String, Long> request,
			@SessionAttribute(name = "loggedInUser", required = false) User user) throws NotFoundException {
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 실패");
	    }
	    
	    Long newPrice = request.get("newPrice");
	    if (newPrice == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입찰가를 입력하세요.");
	    }

		ItemDetailDTO itemDetailDTO = itemDetailService.getItemDetailDTO(itemNo, user);
	    int userNo = user.getUserNo();
	    int sellerNo = itemDetailDTO.getSellerNo();
	    if (userNo == sellerNo) {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("자신의 상품은 입찰할 수 없습니다.");
	    }
	    
	    if (itemDetailService.updatePrice(itemNo, newPrice, user)) {
	        return ResponseEntity.ok("입찰 완료되었습니다.");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입찰가는 현재가보다 1,000원 이상 높아야 합니다.");
	    }
	}
}
