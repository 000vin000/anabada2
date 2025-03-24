package kr.co.anabada.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.service.SellerService;

@RestController
@RequestMapping("/api/sellerinfo")
public class SellerInfoRestController {
	@Autowired
	private SellerService service;
	
	@GetMapping("/{userNo}")
	public ResponseEntity<Seller> sellerInfo(@PathVariable Integer userNo) {
		Seller seller = service.findById(userNo);
		
		return ResponseEntity.ok(seller);
	}
}
