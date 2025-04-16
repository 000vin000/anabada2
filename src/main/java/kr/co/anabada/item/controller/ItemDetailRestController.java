package kr.co.anabada.item.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.item.dto.ItemTimeInfoDTO;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;
import kr.co.anabada.item.exception.AuthenticationRequiredException;
import kr.co.anabada.item.service.ItemDetailService;

@RestController
@RequestMapping("/api/item/detail")
public class ItemDetailRestController {
	@Autowired
	private ItemDetailService itemDetailService;

	@GetMapping
	public ResponseEntity<Integer> getCurrentUser(HttpServletRequest req) {
		return ResponseEntity.ok(itemDetailService.getCurrentUser(req));
	}
	
	@GetMapping("{itemNo}/time-info")
	public ResponseEntity<ItemTimeInfoDTO> getTimeInfo(@PathVariable Integer itemNo) {
		ItemTimeInfoDTO timeInfoDTO = itemDetailService.getTimeInfoDTO(itemNo);
		return ResponseEntity.ok(timeInfoDTO);
	}

	@GetMapping("{itemNo}/remainTime")
	public ResponseEntity<Map<String, Object>> getRemainTime(@PathVariable Integer itemNo) {
		Item item = itemDetailService.getItem(itemNo);
		return ResponseEntity.ok(Map.of(
				"remainTime", item.getTimeLeft().getSeconds(),
				"type", item.isWaiting() ? "시작" : "종료"));
	}

	@GetMapping("{itemNo}/price")
	public ResponseEntity<BigDecimal> getPrice(@PathVariable Integer itemNo) {
		BigDecimal price = itemDetailService.getPrice(itemNo);
		return ResponseEntity.ok(price);
	}

	@GetMapping("{itemNo}/status")
	public ResponseEntity<ItemStatus> getStatus(@PathVariable Integer itemNo) {
		ItemStatus status = itemDetailService.getStatus(itemNo);
		return ResponseEntity.ok(status);
	}

	@GetMapping("user/balance")
	public ResponseEntity<BigDecimal> getCoinBalance(HttpServletRequest req) {
		Integer loggedInUserNo = itemDetailService.getCurrentUser(req);
		if (loggedInUserNo == 0) {
			throw new AuthenticationRequiredException("로그인이 필요한 서비스 입니다.");
		}

		BigDecimal coinBalance = itemDetailService.getCoinBalance(loggedInUserNo);
		return ResponseEntity.ok(coinBalance);
	}

	@PatchMapping("{itemNo}/bid")
	public ResponseEntity<String> updatePrice(
			@PathVariable Integer itemNo,
			@RequestBody Map<String, Long> request,
			HttpServletRequest req) {

		Integer loggedInUserNo = itemDetailService.getCurrentUser(req);
		BigDecimal newPrice = BigDecimal.valueOf(request.get("newPrice"));

		itemDetailService.updatePrice(itemNo, newPrice, loggedInUserNo);
		return ResponseEntity.ok().body("입찰 완료되었습니다.");
	}
}
