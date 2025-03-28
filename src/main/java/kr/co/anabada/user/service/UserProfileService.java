package kr.co.anabada.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.anabada.item.entity.Item;
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

	public UserProfileDTO getUserProfileDTO(Integer userNo) {
		User user = userRepository.findById(userNo)
				.orElseThrow(() -> new EntityNotFoundException("유저 정보가 존재하지 않습니다."));
		Seller seller = sellerService.findById(userNo);
		Buyer buyer = buyerService.getBuyer(userNo);

		Page<ItemSummaryDTO> sellSummaryPage = getSellSummaryDTOs(userNo, 0, 10);
		List<ItemSummaryDTO> sellSummaryList = sellSummaryPage.getContent();

		UserProfileDTO dto = UserProfileDTO.builder()
				.userNo(user.getUserNo())
				.userId(user.getUserId())
				.userNick(user.getUserNick())
				.userCreatedDate(user.getUserCreatedDate())
				.sellerItemCnt(seller.getSellerItemCnt())
				.sellerAvgRating(seller.getSellerAvgRating())
				.sellerGrade(seller.getSellerGrade().getKorean())
				.buyerBidCnt(buyer.getBuyerBidCnt())
				.sellSummaryDTOs(sellSummaryList)
				.build();

		return dto;
	}

	public Page<UserProfileDTO.ItemSummaryDTO> getSellSummaryDTOs(Integer userNo, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Item> items = itemDetailRepository.findRecentBySellerNo(userNo, pageable);

		return items.map(item -> UserProfileDTO.ItemSummaryDTO.builder()
				.itemNo(item.getItemNo())
				.itemTitle(item.getItemTitle())
				.itemPrice(item.getItemPrice())
				.itemStatus(item.getItemStatus().getKorean())
				.itemSoldDate(item.getItemSoldDate())
				.build());
	}
}
