package kr.co.anabada.coin.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.coin.entity.Conversion;
import kr.co.anabada.coin.entity.Conversion.ConversionType;
import kr.co.anabada.coin.repository.ConversionRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class ConversionService {
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ConversionRepository conversionRepo;
	
	private User getUserById(Integer userNo) {
        return userRepo.findById(userNo).orElseThrow(() -> new NoSuchElementException("User not found"));
    }
	
	// 유저 현금 -> 코인 전환 신청 기록
	public Conversion insertConversion(Integer userNo, ConversionType type, BigDecimal conversionAmount) {
		User user = getUserById(userNo);
		System.out.println(user.getUserNick());
		Conversion conversion = new Conversion().builder().userNo(user)
														  .conversionType(type)
														  .conversionAmount(conversionAmount)
														  .build();
		return conversionRepo.save(conversion);
	}

	// 신청 내역
	public List<Conversion> toCoinfindByUserNo(Integer userNo) {
		return conversionRepo.toCoinfindByUserNo(getUserById(userNo));
	}

	// 내역 찾기
	public Optional<Conversion> findByConversionNo(Integer conversionNo) {
		return conversionRepo.findById(conversionNo);
	}
	
	// 신청 취소
	public void deleteConversion(Integer conversionNo) {
		conversionRepo.deleteById(conversionNo);
	}

	// 관리자가 수락
	public int acceptConversion(Integer conversionNo, Admin admin) {
		LocalDateTime now = LocalDateTime.now();
		return conversionRepo.updateConversion(conversionNo, admin, now);
	}
}
