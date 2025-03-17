package kr.co.anabada.mypage.restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/api/item")
public class TodaypickRestController {
	@GetMapping("/viewed")
	public ResponseEntity<List<String>> getViewedItem(@CookieValue(value = "viewedProducts", defaultValue = "[]") String viewedItem) {
		List<String> itemList = new Gson().fromJson(viewedItem, new TypeToken<List<String>>() {}.getType());
		return ResponseEntity.ok(itemList);
	}
}
