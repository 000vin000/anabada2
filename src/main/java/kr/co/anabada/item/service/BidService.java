package kr.co.anabada.item.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.anabada.item.entity.Answer;
import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.mapper.BidMapper;
@Service
public class BidService {
	@Autowired
	private BidMapper bidMapper;
	
	// 입찰하기
	public void insertBid(int itemNo, int userNo, int bidPrice, LocalDateTime bidTime) {
	    Bid bid = new Bid();
	    bid.setItemNo(itemNo);
	    bid.setUserNo(userNo);
	    bid.setBidPrice(bidPrice);
	    bid.setBidTime(bidTime);
        bidMapper.insertBid(bid);
    }
	
	
	// 입찰 기록 조회
	public List<Bid> getBidList(int itemNo) {
		return bidMapper.getBidList(itemNo);

	}
}
