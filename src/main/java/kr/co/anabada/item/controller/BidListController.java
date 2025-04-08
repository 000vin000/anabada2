package kr.co.anabada.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.service.BidListService;

@Controller
public class BidListController {
	@Autowired
	private BidListService bidService;
	
	@GetMapping("/bidList/{itemNo}")
	public String bidList(@PathVariable Integer itemNo, Model model) {
		List<Bid> bidList = bidService.getBidListByItemNo(itemNo);

		if (bidList == null || bidList.isEmpty()) {
			model.addAttribute("error", "입찰기록이 존재하지 않습니다.");
		} else {
			model.addAttribute("bidList", bidList);
		}
		
		return "item/bidList";
	}
}
