package kr.co.anabada.buy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReviewController {
	@GetMapping("/review/write")
	public String writeReview() {
		return "review/writeReviewPage";
	}
}
