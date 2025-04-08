package kr.co.anabada.item.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.coin.service.ChangeCoinService;
import kr.co.anabada.coin.service.GoodsService;
import kr.co.anabada.item.dto.ItemDetailDTO;
import kr.co.anabada.item.entity.Bid;
import kr.co.anabada.item.entity.Bid.BidStatus;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;
import kr.co.anabada.item.repository.BidRepository;
import kr.co.anabada.item.repository.ImageRepository;
import kr.co.anabada.item.repository.ItemDetailRepository;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.entity.User;

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

	public Item getItem(Integer itemNo) {
		return itemDetailRepository.findById(itemNo).orElseThrow(() -> new EntityNotFoundException("물품이 존재하지 않습니다."));
	}

	public ItemDetailDTO getItemDetailDTO(Integer itemNo) {
		Item item = getItem(itemNo);
//		Point_Account account = (userNo != null) ? pointAccountRepository.findById(userNo).orElse(null) : null;
		int imageCount = imageRepository.countByItemNo(item);

//		incrementViewCount(itemNo, userNo);

		ItemDetailDTO dto = ItemDetailDTO.builder()
				.itemNo(item.getItemNo())
				.itemSaleType(item.getItemSaleType().getKorean())
				.itemTitle(item.getItemTitle())
				.itemContent(item.getItemContent())
				.itemStatus(item.getItemStatus().getKorean())
				.itemQuality(item.getItemQuality() != null ? item.getItemQuality().getKorean() : null)
				.itemQuantity(item.getItemQuantity())
				.itemPrice(item.getItemPrice())
				.itemViewCnt(item.getItemViewCnt())
				.itemAvgRating(item.getItemAvgRating())
				.itemLatitude(item.getItemLatitude())
				.itemLongitude(item.getItemLongitude())
				.itemPurcConfirmed(item.isItemPurcConfirmed())
				.itemSaleConfirmed(item.isItemSaleConfirmed())
				.itemSaleStartDate(item.getItemSaleStartDate())
				.itemSaleEndDate(item.getItemSaleEndDate())
				.itemResvStartDate(item.getItemResvStartDate())
				.itemResvEndDate(item.getItemResvEndDate())
				.itemCreatedDate(item.getItemCreatedDate())
				.itemUpdatedDate(item.getItemUpdatedDate())
				.sellerNo(item.getSeller().getUser().getUserNo())
				.sellerNick(item.getSeller().getUser().getUserNick())
				.categoryName(item.getCategory().getCategoryName())
//				.pointBalance(account != null ? account.getPointBalance() : null)
				.imageCount(imageCount).build();

		return dto;
	}

	public Integer getCurrentUser(HttpServletRequest req) {
		UserTokenInfo loggedInUser = jwtAuthHelper.getUserFromRequest(req);
		Integer loggedInUserNo = (loggedInUser != null) ? loggedInUser.getUserNo() : 0;
		return loggedInUserNo;
	}

	@Transactional
	public void incrementViewCount(Integer itemNo, Integer userNo) {
		itemDetailRepository.incrementItemViewCount(itemNo, userNo);
	}

	public BigDecimal getPrice(Integer itemNo) {
		return itemDetailRepository.findItemPriceByItemNo(itemNo)
				.orElseThrow(() -> new EntityNotFoundException("가격 정보를 불러올 수 없습니다."));
	}

	public String getStatus(Integer itemNo) {
		return itemDetailRepository.findItemStatusByItemNo(itemNo).map(ItemStatus::getKorean)
				.orElseThrow(() -> new EntityNotFoundException("상태 정보를 불러올 수 없습니다."));
	}

	public LocalDateTime getSaleStartDate(Integer itemNo) {
		return itemDetailRepository.findItemSaleStartDateByItemNo(itemNo)
				.orElseThrow(() -> new EntityNotFoundException("판매 시작 시간을 불러올 수 없습니다."));
	}

	public LocalDateTime getSaleEndDate(Integer itemNo) {
		return itemDetailRepository.findItemSaleEndDateByItemNo(itemNo)
				.orElseThrow(() -> new EntityNotFoundException("판매 종료 시간을 불러올 수 없습니다."));
	}

	@Transactional
	public boolean updatePrice(Integer itemNo, BigDecimal newPrice, User user) {
		Item item = getItem(itemNo);
		BigDecimal price = item.getItemPrice();

		if (newPrice.compareTo(price.add(new BigDecimal(999))) > 0) {
			try {
				int updatedRows = itemDetailRepository.updateItemPrice(itemNo, newPrice);
				if (updatedRows <= 0) {
					return false;
				}
				
				// TODO update goods
				// TODO insert change_coin

				Bid bid = Bid.builder()
						.user(user)
						.item(item)
						.bidStatus(BidStatus.ACTIVE)
						.bidPrice(newPrice)
						.bidTime(LocalDateTime.now()).build();
				bidRepository.save(bid);
				
				return true;

			} catch (Exception e) {
				throw new RuntimeException("입찰 처리 중 오류가 발생했습니다.", e);
			}
		}
		return false;
	}
}
