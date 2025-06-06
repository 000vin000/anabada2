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
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.exception.AuthenticationRequiredException;
import kr.co.anabada.item.service.ItemDetailService;

@RestController
@RequestMapping("/api/item/detail")
public class ItemDetailRestController {
	@Autowired
	private ItemDetailService service;

	@GetMapping
	public ResponseEntity<Integer> getCurrentUser(HttpServletRequest req) {
		return ResponseEntity.ok(service.getCurrentUser(req));
	}

	@GetMapping("{itemNo}/remainTime")
	public ResponseEntity<Map<String, Object>> getRemainTime(@PathVariable Integer itemNo) {
		Item item = service.getItem(itemNo);
		return ResponseEntity.ok(Map.of(
				"remainTime", item.getTimeLeft().getSeconds(),
				"type", item.isWaiting() ? "시작" : "종료"));
	}

	@GetMapping("{itemNo}/price")
	public ResponseEntity<BigDecimal> getPrice(@PathVariable Integer itemNo) {
		return ResponseEntity.ok(service.getPrice(itemNo));
	}

	@GetMapping("{itemNo}/status")
	public ResponseEntity<String> getStatus(@PathVariable Integer itemNo) {
		return ResponseEntity.ok(service.getStatus(itemNo));
	}

	@GetMapping("user/balance")
	public ResponseEntity<BigDecimal> getCoinBalance(HttpServletRequest req) {
		Integer loggedInUserNo = service.getCurrentUser(req);
		if (loggedInUserNo == 0) {
			throw new AuthenticationRequiredException("로그인이 필요한 서비스 입니다.");
		}

		BigDecimal coinBalance = service.getCoinBalance(loggedInUserNo);
		return ResponseEntity.ok(coinBalance);
	}

	@PatchMapping("{itemNo}/bid")
	public ResponseEntity<String> updatePrice(
			@PathVariable Integer itemNo,
			@RequestBody Map<String, Long> request,
			HttpServletRequest req) {

		Integer loggedInUserNo = service.getCurrentUser(req);
		BigDecimal newPrice = BigDecimal.valueOf(request.get("newPrice"));

		service.updatePrice(itemNo, newPrice, loggedInUserNo);
		return ResponseEntity.ok().body("입찰 완료되었습니다.");
	}
	
	@PatchMapping("/{itemNo}/sale-confirm")
	public ResponseEntity<String> confirmSale(
			@PathVariable Integer itemNo, HttpServletRequest req) {
		Integer loggedInUserNo = service.getCurrentUser(req);
		if (loggedInUserNo == 0) {
			throw new AuthenticationRequiredException("로그인이 필요한 서비스 입니다.");
		}
		service.confirmSale(itemNo, loggedInUserNo);
		return ResponseEntity.ok().body("판매 확정되었습니다.");
	}

	@PatchMapping("/{itemNo}/purc-confirm")
	public ResponseEntity<String> confirmPurchase(
			@PathVariable Integer itemNo, HttpServletRequest req) {
		Integer loggedInUserNo = service.getCurrentUser(req);
		if (loggedInUserNo == 0) {
			throw new AuthenticationRequiredException("로그인이 필요한 서비스 입니다.");
		}
		service.confirmPurchase(itemNo, loggedInUserNo);
		return ResponseEntity.ok().body("구매 확정되었습니다.");
	}
}
