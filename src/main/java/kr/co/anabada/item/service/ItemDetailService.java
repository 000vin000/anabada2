package kr.co.anabada.item.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;
import kr.co.anabada.item.repository.BidRepository;
import kr.co.anabada.item.repository.ItemDetailRepository;
import kr.co.anabada.user.entity.User;

@Service
public class ItemDetailService {
	@Autowired
	private ItemDetailRepository itemDetailRepository;
	
	@Autowired
	private BidRepository bidRepository;
	
	public Item getItem(int itemNo) {
		return itemDetailRepository.findById(itemNo)
	            .orElseThrow(() -> new EntityNotFoundException("물품이 존재하지 않습니다."));
	}

	public Long getPrice(int itemNo) {
		return itemDetailRepository.findItemPriceByItemNo(itemNo)
	            .orElseThrow(() -> new EntityNotFoundException("가격 정보를 불러올 수 없습니다."));
	}

	public String getStatus(int itemNo) {
		return itemDetailRepository.findItemStatusByItemNo(itemNo)
	            .map(ItemStatus::getKorean)
	            .orElseThrow(() -> new EntityNotFoundException("상태 정보를 불러올 수 없습니다."));
	}
	
	public LocalDateTime getSaleStartDate(int itemNo) {
		return itemDetailRepository.findItemSaleStartDateByItemNo(itemNo)
	            .orElseThrow(() -> new EntityNotFoundException("판매 시작 시간을 불러올 수 없습니다."));
	}
	
	public LocalDateTime getSaleEndDate(int itemNo) {
		return itemDetailRepository.findItemSaleEndDateByItemNo(itemNo)
	            .orElseThrow(() -> new EntityNotFoundException("판매 종료 시간을 불러올 수 없습니다."));
	}
	
	public List<String> getAllImages(int itemNo) {
		return null;
	}

	@Transactional
	public boolean updatePrice(int itemNo, Long newPrice, User user) {
		Item item = getItem(itemNo);
		Long price = item.getItemPrice();
		
		if (newPrice >= price + 1000) {
			try {
                int updatedRows = itemDetailRepository.updateItemPrice(itemNo, newPrice);
                if (updatedRows <= 0) {
                    return false;
                }
                
                Bid bid = Bid.builder()
                		.bidPrice(newPrice)
                		.bidTime(LocalDateTime.now())
                		.item(item)
                		.user(user)
                		.build();
                bidRepository.save(bid);
                return true;
                
            } catch (Exception e) {
                throw new RuntimeException("입찰 처리 중 오류가 발생했습니다.", e);
            }
		}
		return false;
	}
}
