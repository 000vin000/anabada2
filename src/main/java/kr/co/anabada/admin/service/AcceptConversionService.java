package kr.co.anabada.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.repository.AcceptConversionRepository;
import kr.co.anabada.coin.entity.Conversion;

@Service
public class AcceptConversionService {
	@Autowired
	private AcceptConversionRepository acptRepo;
	
	// 수락 필요한 전환 신청 리스트
	public List<Conversion> getConList() {
		return acptRepo.getConList();
	}
}
