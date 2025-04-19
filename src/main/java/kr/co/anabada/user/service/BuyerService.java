package kr.co.anabada.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.repository.BuyerRepository;

@Service
public class BuyerService {
	@Autowired
	private BuyerRepository buyerRepository;
	
	public Buyer getBuyer(Integer userNo) {
		return buyerRepository.findByUserUserNo(userNo)
	            .orElseThrow(() -> new EntityNotFoundException("구매자 프로필이 존재하지 않습니다."));
	}
	
	public Integer getBuyerNo(Integer userNo) {
		return buyerRepository.findBuyerNoByUserNo(userNo)
				.orElseThrow(() -> new EntityNotFoundException("구매자 프로필이 존재하지 않습니다."));
	}
}
