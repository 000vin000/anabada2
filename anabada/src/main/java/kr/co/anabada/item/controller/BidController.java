package kr.co.anabada.item.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.service.BidService;

@Controller
public class BidController {
	@Autowired
	private BidService bidService;
		
//	@PostMapping("/item/detail/bid")
//	@ResponseBody 
//	public ResponseEntity<String> insertBid(@RequestParam int itemNo, @RequestParam int userNo, @RequestParam int bidPrice) {
//	    LocalDateTime bidTime = LocalDateTime.now();
//	    bidService.insertBid(itemNo, userNo, bidPrice, bidTime);
//	    return ResponseEntity.ok("입찰이 성공적으로 추가되었습니다."); 
//	}
			
	 // 입찰한 기록 조회
    @GetMapping("/item/bidList/{itemNo}")
    public String getBidList(@PathVariable("itemNo") int itemNo, Model model) {
        List<Bid> list = bidService.getBidList(itemNo);
        model.addAttribute("list", list);
       
        return "item/bidList";
    }

}
