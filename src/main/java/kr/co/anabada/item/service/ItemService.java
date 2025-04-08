package kr.co.anabada.item.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item_Category;
import kr.co.anabada.item.repository.ItemCategoryRepository;
import kr.co.anabada.item.repository.ItemRepository;
import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.entity.Seller.SellerType;
import kr.co.anabada.user.repository.SellerRepository;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class ItemService {
	@Autowired
    private ItemRepository itemRepository;
	@Autowired
	private ItemCategoryRepository categoryRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private SellerRepository sellerRepo;
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
    // ✅ 아이템 조회 (존재하지 않을 경우 예외 발생)
    public Item findById(Integer itemNo) {
        return itemRepository.findById(itemNo)
            .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다: itemNo=" + itemNo));
    }


	 public Item saveItem(Item item, String categoryNo, String userToken) {
		 UserTokenInfo userInfo = jwtTokenHelper.extractUserInfoFromAccessToken(userToken);
		 Optional<Item_Category> category = categoryRepo.findById(categoryNo);
		 Optional<User> user = userRepo.findById(userInfo.getUserNo());
		 Seller byUser = sellerRepo.findByUser(user.get());
		 if (byUser == null) {
			 byUser = Seller.builder().user(user.get()).sellerDesc("").sellerType(SellerType.INDIVIDUAL).build();
			 sellerRepo.save(byUser);
		 }
		 
		 item.setCategory(category.get());
		 item.setSeller(byUser);
		 
		 return itemRepository.save(item);
	 } // jhu : 아이템 등록하는 서비스
}
