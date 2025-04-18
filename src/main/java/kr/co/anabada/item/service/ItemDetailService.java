package kr.co.anabada.item.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.coin.entity.ChangeCoin.ChangeCoinType;
import kr.co.anabada.coin.service.ChangeCoinService;
import kr.co.anabada.coin.service.GoodsService;
import kr.co.anabada.item.dto.ItemDetailDTO;
import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.entity.Bid.BidStatus;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;
import kr.co.anabada.item.exception.BidException;
import kr.co.anabada.item.exception.ResourceNotFoundException;
import kr.co.anabada.item.repository.BidRepository;
import kr.co.anabada.item.repository.ImageRepository;
import kr.co.anabada.item.repository.ItemDetailRepository;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.service.BuyerService;

@Service
public class ItemDetailService {
	@Autowired
	private JwtAuthHelper jwtAuthHelper;
	@Autowired
	private ItemDetailRepository itemDetailRepository;
	@Autowired
	private BidRepository bidRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private ChangeCoinService changeCoinService;
	@Autowired
	private BuyerService buyerService;

	public Item getItem(Integer itemNo) {
		return itemDetailRepository.findById(itemNo)
				.orElseThrow(() -> new ResourceNotFoundException("물품 정보를 불러올 수 없습니다."));
	}

	public ItemDetailDTO getItemDetailDTO(Integer itemNo) {
		Item item = getItem(itemNo);
		int imageCount = imageRepository.countByItemNo(item);
//		incrementViewCount(itemNo, userNo);

		ItemDetailDTO dto = ItemDetailDTO.fromEntity(item, imageCount);
		return dto;
	}

	public Integer getCurrentUser(HttpServletRequest req) {
		UserTokenInfo loggedInUser = jwtAuthHelper.getUserFromRequest(req);
		Integer loggedInUserNo = (loggedInUser != null) ? loggedInUser.getUserNo() : 0;
		return loggedInUserNo;
	}

//	@Transactional
//	public void incrementViewCount(Integer itemNo, Integer userNo) {
//		itemDetailRepository.incrementItemViewCount(itemNo, userNo);
//	}

	public BigDecimal getPrice(Integer itemNo) {
		return itemDetailRepository.findItemPriceByItemNo(itemNo)
				.orElseThrow(() -> new ResourceNotFoundException("가격 정보를 불러올 수 없습니다."));
	}

	public String getStatus(Integer itemNo) {
		return itemDetailRepository.findItemStatusByItemNo(itemNo).map(ItemStatus::getKorean)
				.orElseThrow(() -> new ResourceNotFoundException("상태 정보를 불러올 수 없습니다."));
	}

	public LocalDateTime getSaleStartDate(Integer itemNo) {
		return itemDetailRepository.findItemSaleStartDateByItemNo(itemNo)
				.orElseThrow(() -> new ResourceNotFoundException("판매 시작 시간을 불러올 수 없습니다."));
	}

	public LocalDateTime getSaleEndDate(Integer itemNo) {
		return itemDetailRepository.findItemSaleEndDateByItemNo(itemNo)
				.orElseThrow(() -> new ResourceNotFoundException("판매 종료 시간을 불러올 수 없습니다."));
	}
	
	public BigDecimal getCoinBalance(Integer userNo) {
		BigDecimal coinBalance = goodsService.checkCurrentCashCoin(userNo).getGoodsCoin();
		return coinBalance;
	}

	@Transactional
	private void returnBidCoinToUser(Integer itemNo) {
		Bid topBid = bidRepository.findTopByItemItemNoOrderByBidTimeDesc(itemNo)
				.orElse(null);
		if (topBid != null) {
			Integer targetUserNo = topBid.getBuyer().getUser().getUserNo();
			BigDecimal bidCoin = topBid.getBidPrice();
			BigDecimal targetCoinBalance = getCoinBalance(targetUserNo);
			
			goodsService.updateGoodsCoin(targetUserNo, targetCoinBalance.add(bidCoin));
			changeCoinService.insertChangeCoin(targetUserNo, ChangeCoinType.CANCEL, bidCoin, itemNo);
		}
	}

	@Transactional
	public void updatePrice(Integer itemNo, BigDecimal newPrice, Integer userNo) {
		Item item = getItem(itemNo);
		
		if (!item.isActive()) {
			throw new BidException("입찰 가능한 물품이 아닙니다.");
		}
		if (item.getSeller().getSellerNo() == userNo) {
			throw new BidException("자신의 물품에는 입찰할 수 없습니다.");
		}
		
		BigDecimal itemPrice = item.getItemPrice();
		if (newPrice.compareTo(itemPrice.add(new BigDecimal(999))) <= 0) {
			throw new BidException("입찰가는 현재가보다 1,000원 이상 높아야 합니다.");
		}
		BigDecimal coinBalance = getCoinBalance(userNo);
		if (coinBalance.compareTo(newPrice) < 0) {
			throw new BidException("입찰 금액만큼의 코인이 부족합니다.");
		}
		
		try {
			int updatedRows = itemDetailRepository.updateItemPrice(itemNo, newPrice);
			if (updatedRows <= 0) {
				throw new BidException("현재가 갱신에 실패했습니다.");
			}
			
			goodsService.updateGoodsCoin(userNo, coinBalance.subtract(newPrice));
			changeCoinService.insertChangeCoin(userNo, ChangeCoinType.BID, newPrice, itemNo);
			returnBidCoinToUser(itemNo);

			Bid bid = Bid.builder()
					.buyer(buyerService.getBuyer(userNo))
					.item(item)
					.bidStatus(BidStatus.ACTIVE)
					.bidPrice(newPrice)
					.bidTime(LocalDateTime.now()).build();
			bidRepository.save(bid);

		} catch (BidException e) {
			throw new BidException("입찰 처리에 실패했습니다.");
		}
	}

	@Transactional
	public void confirmSale(Integer itemNo, Integer userNo) {
		int updatedRows = itemDetailRepository.confirmSaleBySeller(itemNo, userNo);
		if (updatedRows == 0) {
			throw new AccessDeniedException("판매 확정에 실패했습니다.");
		} else {
			itemDetailRepository.updateStatusToSoldWhenConfirmed(itemNo);
		}
	}

	@Transactional
	public void confirmPurchase(Integer itemNo, Integer userNo) {
		int updatedRows = itemDetailRepository.confirmPurchaseByBuyer(itemNo, userNo);
		if (updatedRows == 0) {
			throw new AccessDeniedException("구매 확정에 실패했습니다.");
		} else {
			itemDetailRepository.updateStatusToSoldWhenConfirmed(itemNo);
		}
	}
}
