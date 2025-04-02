package kr.co.anabada.buy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import kr.co.anabada.buy.entity.Review;
import kr.co.anabada.buy.repository.ReviewRepository;

@Service
public class ReviewService {
	@Autowired
    private ReviewRepository reviewRepo;

	public void save(Review review) {
		reviewRepo.save(review);
	}

	public List<Review> findAll() {
		return reviewRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
	}
}
