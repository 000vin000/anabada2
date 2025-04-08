package kr.co.anabada.item.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ItemDetailService;

@RestController
@RequestMapping("/api/item/detail")
public class ItemDetailRestController {
	@Autowired
	private ItemDetailService itemDetailService;
	
	@GetMapping
	public Integer getCurrentUser(HttpServletRequest req) {
		return itemDetailService.getCurrentUser(req);
	}
	
	@GetMapping("{itemNo}/remainTime")
	public Map<String, Object> getRemainTime(@PathVariable Integer itemNo) {
		Item item = itemDetailService.getItem(itemNo);
		return Map.of(
				"remainTime", item.getTimeLeft().getSeconds(),
				"type", item.isWaiting() ? "시작": "종료");
	}
	
	@GetMapping("{itemNo}/price")
    public BigDecimal getPrice(@PathVariable Integer itemNo) {
        return itemDetailService.getPrice(itemNo);
    }
	
	@GetMapping("{itemNo}/status")
	public String getStatus(@PathVariable Integer itemNo) {
		return itemDetailService.getStatus(itemNo);
	}
	
	@PatchMapping("{itemNo}/bid")
	public ResponseEntity<String> updatePrice(
			@PathVariable Integer itemNo,
			@RequestBody Map<String, Long> request,
			HttpServletRequest req) throws NotFoundException {
		
		return ResponseEntity.badRequest().body("입찰 기능 리팩토링 중");
	}
}
