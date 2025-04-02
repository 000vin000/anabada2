package kr.co.anabada.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.repository.BidRepository;
import kr.co.anabada.item.repository.ItemDetailRepository;
import kr.co.anabada.user.dto.UserProfileDTO;
import kr.co.anabada.user.dto.UserProfileDTO.ItemSummaryDTO;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class UserProfileService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SellerService sellerService;
	@Autowired
	private BuyerService buyerService;
	@Autowired
	private ItemDetailRepository itemDetailRepository;
	@Autowired
	private BidRepository bidRepository;

	public UserProfileDTO getUserProfileDTO(Integer userNo) {
		User user = userRepository.findById(userNo)
				.orElseThrow(() -> new EntityNotFoundException("유저 정보가 존재하지 않습니다."));
		Seller seller = sellerService.findById(userNo);
		Buyer buyer = buyerService.getBuyer(userNo);

		List<ItemSummaryDTO> recentSellItems = getSellSummaryDTOs(userNo, 0, 10, "recent").getContent();
		List<ItemSummaryDTO> recentBuyItems = getBuySummaryDTOs(userNo, 0, 10, "recent").getContent();

		UserProfileDTO dto = UserProfileDTO.builder()
				.userNo(user.getUserNo())
				.userId(user.getUserId())
				.userNick(user.getUserNick())
				.userCreatedDate(user.getUserCreatedDate())
				.sellerItemCnt(seller.getSellerItemCnt())
				.sellerAvgRating(seller.getSellerAvgRating())
				.sellerGrade(seller.getSellerGrade().getKorean())
				.buyerBidCnt(buyer.getBuyerBidCnt())
				.sellSummaryDTOs(recentSellItems)
				.buySummaryDTOs(recentBuyItems)
				.build();

		return dto;
	}

	public Page<UserProfileDTO.ItemSummaryDTO> getSellSummaryDTOs(Integer targetUserNo, int page, int size, String sort) {
		Pageable pageable = mapToItemSort(page, size, sort);
		Page<Item> items = itemDetailRepository.findBySellerUserUserNo(targetUserNo, pageable);
		return items.map(this::convertToItemSummaryDTO);
	}

	public Page<UserProfileDTO.ItemSummaryDTO> getBuySummaryDTOs(Integer targetUserNo, int page, int size, String sort) {
		Pageable pageable = mapToItemSort(page, size, sort);
		Page<Item> items = itemDetailRepository.findBuysByUserNo(targetUserNo, pageable);
		return items.map(this::convertToItemSummaryDTO);
	}

	private Pageable mapToItemSort(int page, int size, String sort) {
		Pageable pageable;

		switch (sort) {
		case "priceAsc":
			pageable = PageRequest.of(page, size, Sort.by("itemPrice").ascending());
			break;
		case "priceDesc":
			pageable = PageRequest.of(page, size, Sort.by("itemPrice").descending());
			break;
		case "titleAsc":
			pageable = PageRequest.of(page, size, Sort.by("itemTitle").ascending());
			break;
		case "recent":
		default:
			pageable = PageRequest.of(page, size, Sort.by("itemCreatedDate").descending());
			break;
		}

		return pageable;
	}

	private ItemSummaryDTO convertToItemSummaryDTO(Item item) {
		return ItemSummaryDTO.builder()
				.itemNo(item.getItemNo())
				.itemTitle(item.getItemTitle())
				.itemPrice(item.getItemPrice())
				.itemStatus(item.getItemStatus().getKorean())
				.itemSoldDate(item.getItemSoldDate())
				.viewCount(item.getItemViewCnt())
				.bidCount(bidRepository.countByItemItemNo(item.getItemNo()))
				.build();
	}
}
