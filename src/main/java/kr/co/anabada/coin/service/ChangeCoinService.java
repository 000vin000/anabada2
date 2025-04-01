package kr.co.anabada.coin.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.coin.dto.ChangeCoinDTO;
import kr.co.anabada.coin.entity.ChangeCoin;
import kr.co.anabada.coin.repository.ChangeCoinRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class ChangeCoinService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ChangeCoinRepository coinRepo;
	
	private User getUserById(Integer userNo) {
        return userRepo.findById(userNo).orElseThrow(() -> new NoSuchElementException("User not found"));
    }
	
	// 코인 사용 내역 받아오기
	public List<ChangeCoinDTO> getUseList(Integer userNo) {
		User user = getUserById(userNo);
		List<ChangeCoin> coinList = coinRepo.findByUser(user);
		
		return coinList.stream()
                .map(ChangeCoinDTO::new)
                .collect(Collectors.toList());
	}
}
