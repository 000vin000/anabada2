package kr.co.anabada.coin.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.entity.Admin;
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
	public Account insertAccountDeposit(Integer userNo, PayType payType, BigDecimal amount) {
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
	
	// 유처 현금 출금 정보 등록
	public Account insertAccountWithdrawal(Integer userNo, BigDecimal amount, String withdrawalBank, String withdrawalAccount) {
		User user = getUserById(userNo);
		Goods goods = goodsRepo.findByUser(user);
		
		Account account = new Account().builder().userNo(user)
												 .accountType(AccountType.WITHDRAWAL)
												 .accountAmount(amount)
												 .accountBalance(goods.getGoodsCash())
												 .accountBankForWithdraw(withdrawalBank)
												 .accountNumberForWithdraw(withdrawalAccount)
												 .build();
		return accountRepo.save(account);
	}

	// 현금 충전 기록 리스트
	public List<Account> getChargeList(Integer userNo) {
		User user = getUserById(userNo);
		return accountRepo.depositFindByUser(user);
	}
	
	// 현금 출금 기록 리스트
	public List<Account> getWithdrawList(Integer userNo) {
		User user = getUserById(userNo);
		return accountRepo.withdrawFindByUser(user);
	}

	// 번호로 찾기
	public Optional<Account> findByAccountNo(Integer accountNo) {
		return accountRepo.findById(accountNo);
	}

	// 내역 삭제
	public void deleteAccount(Integer accountNo) {
		accountRepo.deleteById(accountNo);
	}

	// 관리자 수락
	public int acceptAccount(Integer accountNo, Admin admin) {
		LocalDateTime now = LocalDateTime.now();
		return accountRepo.updateAccount(accountNo, admin, now);
	}
}
