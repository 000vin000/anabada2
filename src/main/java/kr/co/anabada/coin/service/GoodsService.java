package kr.co.anabada.coin.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.repository.GoodsRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class GoodsService {
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	GoodsRepository goodsRepo;
	
	private User getUserById(Integer userNo) {
        return userRepo.findById(userNo).orElseThrow(() -> new NoSuchElementException("User not found"));
    }
	
	// 현재 보유금액 확인
	public Goods checkCurrentCashCoin(Integer userNo) {
		User user = getUserById(userNo);
		return goodsRepo.findByUser(user);
	}

	// 사용자 정보 삽입
	public Goods insertUserGoods(Integer userNo) {
		User user = getUserById(userNo);
		Goods goods = new Goods();
		goods.setUser(user);
		return goodsRepo.save(goods);
	}

	// 사용자 보유 잔액 업데이트
	public Goods updateGoodsCash(Integer userNo, BigDecimal insertAmount) {
		User user = getUserById(userNo);
		Goods goods = goodsRepo.findByUser(user);
		goods.setGoodsCash(insertAmount);
		return goodsRepo.save(goods);
	}
	
	// 사용자 보유 코인 업데이트
	public Goods updateGoodsCoin(Integer userNo, BigDecimal insertAmount) {
		User user = getUserById(userNo);
		Goods goods = goodsRepo.findByUser(user);
		goods.setGoodsCoin(insertAmount);
		return goodsRepo.save(goods);
	}
}
