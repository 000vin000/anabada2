package kr.co.anabada.coin.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.coin.entity.Account;
import kr.co.anabada.coin.entity.Account.AccountType;
import kr.co.anabada.coin.entity.Account.PayType;
import kr.co.anabada.coin.entity.Goods;
import kr.co.anabada.coin.repository.AccountRepository;
import kr.co.anabada.coin.repository.GoodsRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class AccountService {
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	GoodsRepository goodsRepo;
	
	private User getUserById(Integer userNo) {
        return userRepo.findById(userNo).orElseThrow(() -> new NoSuchElementException("User not found"));
    }
	
	// 유저 현금 충전 정보 등록
	public Account insertAccount(Integer userNo, PayType payType, BigDecimal amount) {
		User user = getUserById(userNo);
		Goods goods = goodsRepo.findByUser(user);
		BigDecimal cash = goods.getGoodsCash().add(amount);
		Account account = new Account().builder().userNo(user)
												 .accountType(AccountType.DEPOSIT)
												 .accountAmount(amount)
												 .accountBalance(cash)
												 .accountPayType(payType)
												 .build();
		return accountRepo.save(account);
	}

	// 현금 충전 기록 리스트
	public List<Account> getChargeList(Integer userNo) {
		User user = getUserById(userNo);
		return accountRepo.depositFindByUser(user);
	}
}
