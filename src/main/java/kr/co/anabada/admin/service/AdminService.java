package kr.co.anabada.admin.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.repository.AdminRepository;
import kr.co.anabada.coin.entity.Account;
import kr.co.anabada.coin.entity.Account.AccountType;
import kr.co.anabada.coin.entity.ChangeCoin.ChangeCoinType;
import kr.co.anabada.coin.entity.Conversion;
import kr.co.anabada.coin.entity.Conversion.ConversionType;
import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.repository.AccountRepository;
import kr.co.anabada.coin.repository.ConversionRepository;
import kr.co.anabada.coin.repository.GoodsRepository;
import kr.co.anabada.coin.service.ChangeCoinService;
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
	
	@Autowired
	private ChangeCoinService coinService;
	
	@Autowired
	private AccountRepository accountRepo;
	
	private User getUserById(Integer userNo) {
        return userRepo.findById(userNo).orElseThrow(() -> new NoSuchElementException("User not found"));
    }
	
	// 관리자 찾기
	public Admin findByUserNo(Integer userNo) {
		User user = getUserById(userNo);
		return adminRepo.findByUser(user);
	}
	
	// conversionNo의 userNo로 goods 정보를 가져와 goodsCoin update
	public void updateCoin(Integer conversionNo) {
		Optional<Conversion> con = conRepo.findById(conversionNo);
		
		if (con.isPresent() && con.get().getConversionType().equals(ConversionType.TOCOIN)) {
			User user = con.get().getUserNo();
			BigDecimal amount = con.get().getConversionAmount();
			
			Goods goods = goodsRepo.findByUser(user);
			goods.setGoodsCoin(goods.getGoodsCoin().add(amount));
			goodsRepo.save(goods);
			
			// 코인 변동 내역에 삽입
			coinService.insertChangeCoin(user.getUserNo(), ChangeCoinType.CHARGE, amount);
			
			System.out.println(user.getUserId() + " 코인 업데이트 완료");
		} else if (con.isPresent() && con.get().getConversionType().equals(ConversionType.TOCASH)) {
			User user = con.get().getUserNo();
			BigDecimal amount = con.get().getConversionAmount();
			
			Goods goods = goodsRepo.findByUser(user);
			goods.setGoodsCash(goods.getGoodsCash().add(amount));
			goodsRepo.save(goods);
			
			// 코인 변동 내역에 삽입
			coinService.insertChangeCoin(user.getUserNo(), ChangeCoinType.CASH, amount);
			
			System.out.println(user.getUserId() + "현금 업데이트 완료");
		}
	}

	// accountNo의 userNo로 goods 정보를 가져와 goodsCash update (입금일 때만)
	public void updateCash(Integer accountNo) {
		Optional<Account> acc = accountRepo.findById(accountNo);
		
		if (acc.isPresent() && acc.get().getAccountType().equals(AccountType.DEPOSIT)) {
			User user = acc.get().getUserNo();
			BigDecimal amount = acc.get().getAccountAmount();
			
			Goods goods = goodsRepo.findByUser(user);
			goods.setGoodsCash(goods.getGoodsCash().add(amount));
			goodsRepo.save(goods);
			
			System.out.println(user.getUserId() + " 입금 확인");
		}
	}
}

