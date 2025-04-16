package kr.co.anabada.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;
import kr.co.anabada.item.repository.BidRepository;
import kr.co.anabada.item.repository.ItemDetailRepository;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.dto.UserProfileDTO;
import kr.co.anabada.user.dto.UserProfileDTO.ItemSummaryDTO;
import kr.co.anabada.user.dto.UserProfileDashboardDTO;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.BuyerRepository;
import kr.co.anabada.user.repository.SellerRepository;
import kr.co.anabada.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
	private SellerRepository sellerRepository;
	@Autowired
	private BuyerRepository buyerRepository;

	public UserProfileDTO getUserProfileDTO(Integer targetUserNo) {
		User user = userRepository.findById(targetUserNo)
				.orElseThrow(() -> new EntityNotFoundException("유저 정보가 존재하지 않습니다."));
		
		Seller seller = sellerRepository.findByUserUserNo(targetUserNo).orElse(null);
		Buyer buyer = buyerRepository.findByUserUserNo(targetUserNo).orElse(null);

		UserProfileDTO dto = UserProfileDTO.fromEntity(user, seller, buyer);

		return dto;
	}

	public UserProfileDashboardDTO getUserProfileDashboardDTO(Integer targetUserNo) {
		Seller seller = sellerRepository.findByUserUserNo(targetUserNo).orElse(null);
		Buyer buyer = buyerRepository.findByUserUserNo(targetUserNo).orElse(null);
		
		UserProfileDashboardDTO.SellerDashboardDTO sellerDashboard =
				UserProfileDashboardDTO.SellerDashboardDTO.fromEntity(seller);
		
		UserProfileDashboardDTO.BuyerDashboardDTO buyerDashboard =
				UserProfileDashboardDTO.BuyerDashboardDTO.fromEntity(buyer);
		
		UserProfileDashboardDTO dto = UserProfileDashboardDTO.fromEntity(sellerDashboard, buyerDashboard);
		
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
		Page<Item> items = getItemsByRoleAndOptionalStatus(role, targetUserNo, status, pageable);

		if (isOwnProfile) {
			Integer buyerNo = buyerRepository.findBuyerNoByUserUserNo(targetUserNo).orElse(null);
			if (buyerNo != null) {
				return items.map(item -> getAuthenticatedItemSummaryDTO(item, buyerNo));
			}
		}
		return items.map(item -> getItemSummaryDTO(item));
	}

	private Page<Item> getItemsByRoleAndOptionalStatus(UserRole role, Integer userNo, String status, Pageable pageable) {
		ItemStatus statusEnum = null;
		if (!status.equalsIgnoreCase("all") && status != null && !status.trim().isEmpty()) {
			try {
				statusEnum = ItemStatus.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException ignore) {
				log.warn("status 변환 실패: 입력값 '{}'", status);
			}
		}
		
		switch (role) {
		case SELLER:
			return itemDetailRepository.findBySellerUserNoAndOptionalItemStatus(
						userNo, statusEnum, pageable);
		case BUYER:
			return itemDetailRepository.findByBuyerUserNoAndOptionalItemStatus(
					userNo, statusEnum, pageable);
		default:
			return Page.empty(pageable);
		}
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

	private ItemSummaryDTO getItemSummaryDTO(Item item) {
		int bidCount = bidRepository.countByItemItemNo(item.getItemNo());
		ItemSummaryDTO dto = ItemSummaryDTO.fromEntity(item, bidCount);
		return dto;
	}

	private UserProfileDTO.AuthenticatedItemSummaryDTO getAuthenticatedItemSummaryDTO(Item item, Integer buyerNo) {
		UserProfileDTO.AuthenticatedItemSummaryDTO dto =
				new UserProfileDTO.AuthenticatedItemSummaryDTO();
		BeanUtils.copyProperties(getItemSummaryDTO(item), dto);
//		dto.setReviewed(); // TODO 리뷰버튼 노출 로직 구현 필요

		return dto;
	}

	private enum UserRole {
		SELLER, BUYER
	}
}
