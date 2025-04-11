package kr.co.anabada.user.service;

import java.util.Arrays;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.buy.repository.ReviewRepository;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Bid.BidStatus;
import kr.co.anabada.item.entity.Item.ItemStatus;
import kr.co.anabada.item.repository.BidRepository;
import kr.co.anabada.item.repository.ItemDetailRepository;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.dto.UserProfileDTO;
import kr.co.anabada.user.dto.UserProfileDTO.ItemSummaryDTO;
import kr.co.anabada.user.dto.UserProfileDetailDTO.AuthenticatedItemSummaryDTO;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class UserProfileService {
	@Autowired
	private JwtAuthHelper jwtAuthHelper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ItemDetailRepository itemDetailRepository;
	@Autowired
	private BidRepository bidRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private SellerService sellerService;
	@Autowired
	private BuyerService buyerService;

	public UserProfileDTO getUserProfileDTO(Integer targetUserNo) {
		User user = userRepository.findById(targetUserNo)
				.orElseThrow(() -> new EntityNotFoundException("유저 정보가 존재하지 않습니다."));
		Seller seller = sellerService.findById(targetUserNo);
		Buyer buyer = buyerService.getBuyer(targetUserNo);

		UserProfileDTO dto = UserProfileDTO.builder()
				.userNo(user.getUserNo())
				.userId(user.getUserId())
				.userNick(user.getUserNick())
				.userCreatedDate(user.getUserCreatedDate())
				.sellerItemCnt(seller.getSellerActiveItemCnt())
				.sellerAvgRating(seller.getSellerAvgRating())
				.sellerGrade(seller.getSellerGrade().getKorean())
				.buyerBidCnt(buyer.getBuyerBidItemCnt())
				.build();

		return dto;
	}

	public Integer getCurrentUser(HttpServletRequest req) {
		UserTokenInfo loggedInUser = jwtAuthHelper.getUserFromRequest(req);
		Integer loggedInUserNo = (loggedInUser != null) ? loggedInUser.getUserNo() : 0;
		return loggedInUserNo;
	}

	public Page<UserProfileDTO.ItemSummaryDTO> getSellItems(
			Integer targetUserNo, boolean isOwnProfile, int page, int size, String status, String sort) {
		return getItems(UserRole.SELLER, targetUserNo, isOwnProfile, page, size, status, sort);
	}

	public Page<UserProfileDTO.ItemSummaryDTO> getBuyItems(
			Integer targetUserNo, boolean isOwnProfile, int page, int size, String status, String sort) {
		return getItems(UserRole.BUYER, targetUserNo, isOwnProfile, page, size, status, sort);
	}

	private Page<UserProfileDTO.ItemSummaryDTO> getItems(
			UserRole role, Integer targetUserNo, boolean isOwnProfile, int page, int size, String status, String sort) {
		Pageable pageable = getPageableBySort(page, size, sort);
		Page<Item> items;
		
		items = Arrays.stream(ItemStatus.values()).anyMatch(s -> s.name().equalsIgnoreCase(status))
				? getItemsByRoleAndStatus(role, targetUserNo, status, pageable)
				: getItemsByRole(role, targetUserNo, pageable);

		if (isOwnProfile) {
			Integer buyerNo = buyerService.getBuyerNo(targetUserNo);
			items.map(item -> convertToAuthenticatedItemSummaryDTO(item, buyerNo));
		}
		return items.map(item -> convertToItemSummaryDTO(item));
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
		ItemSummaryDTO dto = ItemSummaryDTO.builder()
				.itemNo(item.getItemNo())
				.itemTitle(item.getItemTitle())
				.itemPrice(item.getItemPrice())
				.itemStatus(item.getItemStatus().getKorean())
				.itemSoldDate(item.getItemSoldDate())
				.viewCount(item.getItemViewCnt())
				.bidCount(bidRepository.countByItemItemNo(item.getItemNo()))
				.build();

		return dto;
	}

	private AuthenticatedItemSummaryDTO convertToAuthenticatedItemSummaryDTO(Item item, Integer buyerNo) {
		AuthenticatedItemSummaryDTO dto = new AuthenticatedItemSummaryDTO();
		BeanUtils.copyProperties(convertToItemSummaryDTO(item), dto);
		dto.setReviewed(reviewRepository.existsByBidItemItemNoAndBidBuyerBuyerNoAndBidBidStatus(
				item.getItemNo(), buyerNo, BidStatus.WINNING));

		return dto;
	}

	private enum UserRole {
		SELLER, BUYER
	}
}
