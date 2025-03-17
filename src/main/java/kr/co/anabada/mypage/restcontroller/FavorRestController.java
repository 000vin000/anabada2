package kr.co.anabada.mypage.restcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.anabada.mypage.service.FavorService;
import kr.co.anabada.user.entity.User;

@RestController
@RequestMapping("/api/favor")
public class FavorRestController {
	@Autowired
	private FavorService service;
	
	@GetMapping("/{itemNo}")
    public ResponseEntity<?> checkFavorite(@SessionAttribute(name = "loggedInUser", required = false) User user, @PathVariable int itemNo) {
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("isFavorite", false));
		}
		boolean isFavorite = service.isFavorite(user.getUserNo(), itemNo);
	    return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }
	
	@PostMapping("/{itemNo}")
	public ResponseEntity<?> addFavor(@SessionAttribute(name = "loggedInUser", required = false) User user, @PathVariable int itemNo) {
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요한 기능입니다"));
		}
		
		boolean isFavorited = service.toggleFavorite(user.getUserNo(), itemNo);
        return ResponseEntity.ok(isFavorited);
	}
	
	@DeleteMapping("/{itemNo}")
	public String removeFavor(@SessionAttribute(name = "loggedInUser", required = false) User user, @PathVariable int itemNo) {
		service.removeFavor(user.getUserNo(), itemNo);
		return "즐겨찾기를 해제했습니다.";
	}
}
