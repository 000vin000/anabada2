package kr.co.anabada.user.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.repository.ItemRepository;
import kr.co.anabada.user.entity.FavorItem;
import kr.co.anabada.user.entity.FavorSeller;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.FavorItemRepository;
import kr.co.anabada.user.repository.FavorSellerRepository;
import kr.co.anabada.user.repository.SellerRepository;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class FavorService {
	@Autowired
	UserRepository userRepo;
	@Autowired
	ItemRepository itemRepo;
	@Autowired
	SellerRepository sellerRepo;
	@Autowired
	FavorItemRepository favorItemRepo;
	@Autowired
	FavorSellerRepository favorSellerRepo;
	
    private User getUserById(Integer userNo) {
        return userRepo.findById(userNo).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    private Item getItemById(Integer itemNo) {
        return itemRepo.findById(itemNo).orElseThrow(() -> new NoSuchElementException("Item not found"));
    }
    
    private Seller getSellerById(Integer sellerNo) {
    	return sellerRepo.findById(sellerNo).orElseThrow(() -> new NoSuchElementException("Seller not found"));
    }
	
    // 물품 즐겨찾기
	public boolean isFavorItem(Integer userNo, Integer itemNo) {
        User user = getUserById(userNo);
        Item item = getItemById(itemNo);
		return favorItemRepo.countByUserAndItem(user, item) > 0;
	}
	
	public void deleteFavorItem(Integer userNo, Integer itemNo) {
        User user = getUserById(userNo);
        Item item = getItemById(itemNo);
		favorItemRepo.deleteByUserAndItem(user, item);
	}

	public boolean toggleFavorItem(Integer userNo, Integer itemNo) {
        User user = getUserById(userNo);
        Item item = getItemById(itemNo);
		FavorItem favor = favorItemRepo.findByUserAndItem(user, item);
		if (favor != null) {
			favorItemRepo.delete(favor);
			return false; // 삭제됨
    	} else {
    		favorItemRepo.save(FavorItem.builder().user(user).item(item).build());
		    return true; // 추가됨
    	}
	}
	
	// 판매자 즐겨찾기
	public boolean isFavorSeller(Integer userNo, Integer sellerNo) {
        User user = getUserById(userNo);
        Seller seller = getSellerById(sellerNo);
		return favorSellerRepo.countByUserAndSeller(user, seller) > 0;
	}
	
	public void deleteFavorSeller(Integer userNo, Integer sellerNo) {
        User user = getUserById(userNo);
        Seller seller = getSellerById(sellerNo);
        favorSellerRepo.deleteByUserAndSeller(user, seller);
	}

	public boolean toggleFavorSeller(Integer userNo, Integer sellerNo) {
        User user = getUserById(userNo);
        Seller seller = getSellerById(sellerNo);
		FavorSeller favor = favorSellerRepo.findByUserAndSeller(user, seller);
		if (favor != null) {
			favorSellerRepo.delete(favor);
			return false; // 삭제됨
    	} else {
    		favorSellerRepo.save(FavorSeller.builder().user(user).seller(seller).build());
		    return true; // 추가됨
    	}
	}
}
