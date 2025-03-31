package kr.co.anabada.buy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.buy.entity.Review;
import kr.co.anabada.buy.service.ReviewService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {
    @Autowired
    private JwtAuthHelper jwtAuthHelper;
	@Autowired
    private ReviewService service;
    
    @PostMapping("/write")
    public ResponseEntity<?> createReview(@RequestBody Review review, HttpServletRequest req) {
    	UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
    	
        if (review.getReviewRating() < 0.5 || review.getReviewRating() > 5.0) {
            return ResponseEntity.badRequest().body("별점은 0.5 ~ 5.0 사이여야 합니다.");
        }

        service.save(review);
        return ResponseEntity.ok("리뷰 등록 성공");
    }

    @GetMapping
    public List<Review> getReviews() {
        return service.findAll();
    }
}
