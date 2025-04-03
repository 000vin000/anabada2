package kr.co.anabada.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.repository.DepositWithdrawalRepository;
import kr.co.anabada.coin.entity.Account;

@Service
public class DepositWithdrawalService {
	@Autowired
	private DepositWithdrawalRepository dewiRepo;
	
	// 확인 필요한 입출금 리스트
	public List<Account> getAccountList() {
		return dewiRepo.getAccountList();
	}
}
