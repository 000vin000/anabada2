package kr.co.anabada.admin.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.repository.AdminRepository;
import kr.co.anabada.coin.entity.Conversion;
import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.repository.ConversionRepository;
import kr.co.anabada.coin.repository.GoodsRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class AdminService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private ConversionRepository conRepo;
	
	@Autowired
	private GoodsRepository goodsRepo;
	
	private User getUserById(Integer userNo) {
        return userRepo.findById(userNo).orElseThrow(() -> new NoSuchElementException("User not found"));
    }
	
	// 관리자 찾기
	public Admin findByUserNo(Integer userNo) {
		User user = getUserById(userNo);
		return adminRepo.findByUserNo(user);
	}
	
	// conversionNo의 userNo로 goods 정보를 가져와 goodsCoin update
	public void updateCoin(Integer conversionNo) {
		Optional<Conversion> con = conRepo.findById(conversionNo);
		
		if (con.isPresent()) {
			User user = con.get().getUserNo();
			BigDecimal amount = con.get().getConversionAmount();
			
			Goods goods = goodsRepo.findByUser(user);
			goods.setGoodsCoin(goods.getGoodsCoin().add(amount));
			goodsRepo.save(goods);
			System.out.println("코인 업데이트 완료");
		}
	}
}
