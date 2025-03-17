package kr.co.anabada.mypage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.mypage.dto.BuylistDto;
import kr.co.anabada.mypage.mapper.BuyMapper;

@Service
public class BuyService {
	@Autowired
	BuyMapper mapper;
	
	public List<BuylistDto> selectAllBuy(int userNo) {
		return mapper.selectAllBuy(userNo);
	}
}
