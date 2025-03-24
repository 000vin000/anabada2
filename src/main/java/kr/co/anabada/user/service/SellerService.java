package kr.co.anabada.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.repository.ItemRepository;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.SellerRepository;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class SellerService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private SellerRepository sellerRepo;
	@Autowired
	private ItemRepository itemRepo;

	public Seller findById(Integer userNo) {
		Optional<User> user = userRepo.findById(userNo);
		Seller seller = sellerRepo.findByUser(user.get());
		if (user.isPresent() && seller != null) {
			seller.setUser(user.get());
			return seller; 
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "판매자를 찾을 수 없습니다.");
		}
	}
	
	public List<Item> findBySeller(Integer userNo) {
		Optional<User> user = userRepo.findById(userNo);
		Seller seller = sellerRepo.findByUser(user.get());
		List<Item> itemList = itemRepo.findBySeller(seller);
		
		return itemList;
	}
}
