package kr.co.anabada.user.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;
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

		List<ItemSummaryDTO> recentSellItems = getItems(
				UserRole.SELLER, userNo, 0, 8, "all", "recent").getContent();
		List<ItemSummaryDTO> recentBuyItems = getItems(
				UserRole.BUYER, userNo, 0, 8, "all", "recent").getContent();

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

	public Page<UserProfileDTO.ItemSummaryDTO> getSellItems(
			Integer targetUserNo, int page, int size, String status, String sort) {
		return getItems(UserRole.SELLER, targetUserNo, page, size, status, sort);
	}

	public Page<UserProfileDTO.ItemSummaryDTO> getBuyItems(
			Integer targetUserNo, int page, int size, String status, String sort) {
		return getItems(UserRole.BUYER, targetUserNo, page, size, status, sort);
	}

	private Page<UserProfileDTO.ItemSummaryDTO> getItems(
			UserRole role, Integer targetUserNo, int page, int size, String status, String sort) {
		Pageable pageable = getPageableBySort(page, size, sort);
		Page<Item> items;

		if (Arrays.stream(ItemStatus.values()).anyMatch(s -> s.name().equalsIgnoreCase(status))) {
			items = getItemsByRoleAndStatus(role, targetUserNo, status, pageable);
		} else {
			items = getItemsByRole(role, targetUserNo, pageable);
		}

		return items.map(this::convertToItemSummaryDTO);
	}

	private Page<Item> getItemsByRole(UserRole role, Integer userNo, Pageable pageable) {
		return role == UserRole.SELLER
				? itemDetailRepository.findBySellerUserUserNo(userNo, pageable)
				: itemDetailRepository.findByBuyerNoAndOptionalItemStatus(userNo, null, pageable);
	}

	private Page<Item> getItemsByRoleAndStatus(UserRole role, Integer userNo, String status, Pageable pageable) {
		ItemStatus statusEnum = ItemStatus.valueOf(status.toUpperCase());
		return role == UserRole.SELLER
				? itemDetailRepository.findBySellerUserUserNoAndItemStatus(userNo, statusEnum, pageable)
				: itemDetailRepository.findByBuyerNoAndOptionalItemStatus(userNo, statusEnum, pageable);
	}

	private Pageable getPageableBySort(int page, int size, String sort) {
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

	private enum UserRole {
		SELLER, BUYER
	}
}
