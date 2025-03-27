package kr.co.anabada.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.service.FavorService;

@RestController
@RequestMapping("/api/favor")
public class FavorRestController {
	@Autowired
	private FavorService service;
	@Autowired
	private JwtAuthHelper jwtAuthHelper;
	
	// 물품
	@GetMapping("/item/{itemNo}")
    public ResponseEntity<?> checkFavorItem(@PathVariable Integer itemNo, HttpServletRequest req) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		if (user == null) {
			return ResponseEntity.ok(Map.of("isFavorite", false));
		}
		boolean isFavorite = service.isFavorItem(user.getUserNo(), itemNo);
	    return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }
	
	@PostMapping("/item/{itemNo}")
	public ResponseEntity<?> addFavorItem(@PathVariable Integer itemNo, HttpServletRequest req) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요한 기능입니다"));
		}
		boolean isFavorited = service.toggleFavorItem(user.getUserNo(), itemNo);
        return ResponseEntity.ok(isFavorited);
	}
	
	@DeleteMapping("/item/{itemNo}")
	public String deleteFavorItem(@PathVariable Integer itemNo, HttpServletRequest req) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		
		service.deleteFavorItem(user.getUserNo(), itemNo);
		return "즐겨찾기를 해제했습니다.";
	}
	
	// 판매자
	@GetMapping("/seller/{sellerNo}")
    public ResponseEntity<?> checkFavorSeller(@PathVariable Integer sellerNo, HttpServletRequest req) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		if (user == null) {
			return ResponseEntity.ok(Map.of("isFavorite", false));
		}
		boolean isFavorite = service.isFavorSeller(user.getUserNo(), sellerNo);
	    return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }
	
	@PostMapping("/seller/{sellerNo}")
	public ResponseEntity<?> addFavorSeller(@PathVariable Integer sellerNo, HttpServletRequest req) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요한 기능입니다"));
		}
		boolean isFavorited = service.toggleFavorSeller(user.getUserNo(), sellerNo);
        return ResponseEntity.ok(isFavorited);
	}
	
	@DeleteMapping("/seller/{sellerNo}")
	public String deleteFavorSeller(@PathVariable Integer sellerNo, HttpServletRequest req) {
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		service.deleteFavorSeller(user.getUserNo(), sellerNo);
		
		return "즐겨찾기를 해제했습니다.";
	}
}