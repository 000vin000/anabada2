package kr.co.anabada.item.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.service.ItemDetailService;
import kr.co.anabada.item.service.BidService;
import kr.co.anabada.user.mapper.UserMapper;
import kr.co.anabada.user.entity.User;

@Controller
@RequestMapping("/item/detail/{itemNo}")
public class ItemDetailController {
	@Autowired
	private ItemDetailService itemDetailService;
	
	@Autowired
	private BidService bidService;
	
	@Autowired
	private UserMapper mapper;
	
	@GetMapping
	public String getItemDetail(@PathVariable int itemNo, Model model) {
		Item item = itemDetailService.getItemByNo(itemNo);
		List<String> images = itemDetailService.getAllImages(itemNo);
		String userNick = mapper.selectUserNick(item.getUserNo());
		
		model.addAttribute("item", item);
		model.addAttribute("images", images);
		model.addAttribute("userNick", userNick);
		
		return "item/itemDetail";
	}
	
	private long calculateRemainTime(LocalDateTime itemDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, itemDate);
        return duration.getSeconds();
    }
	
	@GetMapping("/remainTime/{type}")
	@ResponseBody
	public long getRemainTime(@PathVariable int itemNo, @PathVariable String type) {
		switch(type) {
		case "start":
			return calculateRemainTime(itemDetailService.getItemStart(itemNo));
		case "end":
			return calculateRemainTime(itemDetailService.getItemEnd(itemNo));
		}
		return 0;
	}
	
	@GetMapping("/currentPrice")
	@ResponseBody
    public int getCurrentPrice(@PathVariable int itemNo) {
        return itemDetailService.getCurrentPrice(itemNo);
    }
	
	@GetMapping("/currentState")
	@ResponseBody
	public String getCurrentState(@PathVariable int itemNo) {
		String state = itemDetailService.getCurrentState(itemNo);
		return Item.getItemStatusInKorean(state);
	}
	
	@PatchMapping("/bid")
	@ResponseBody
	public ResponseEntity<String> updateItemPrice(
			@PathVariable int itemNo, @RequestParam int itemPrice,
			@SessionAttribute(name = "loggedInUser", required = false) User user) {
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 실패");
	    }

		Item item = itemDetailService.getItemByNo(itemNo);
	    int userNo = user.getUserNo();
	    int itemUserNo = item.getUserNo();
	    if (userNo == itemUserNo) {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("자신의 상품은 입찰할 수 없습니다.");
	    }
	    
	    int price = item.getItemPrice();
	    if (itemPrice <= price) {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입찰가는 현재가보다 높아야 합니다.");
	    }
	    
	    int row = itemDetailService.updatePrice(itemNo, itemPrice);
	    if (row > 0) {
	        LocalDateTime bidTime = LocalDateTime.now();
	        bidService.insertBid(itemNo, userNo, itemPrice, bidTime);
	        return ResponseEntity.ok("입찰 성공");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입찰 실패");
	    }
	}
}
