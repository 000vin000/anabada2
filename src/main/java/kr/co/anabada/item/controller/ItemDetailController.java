package kr.co.anabada.item.controller;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.item.dto.ItemDetailDTO;
import kr.co.anabada.item.service.ItemDetailService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/item/detail/{itemNo}")
public class ItemDetailController {
	@Autowired
	private ItemDetailService itemDetailService;
	
	@Autowired
	private JwtAuthHelper jwtAuthHelper;
	
	// 정빈 수정
	@GetMapping
	public String getItemDetail(
			@PathVariable Integer itemNo,
			Model model,
			HttpServletRequest req) throws NotFoundException {

		// 1. 상품 정보 조회
		ItemDetailDTO item = itemDetailService.getItemDetailDTO(itemNo);

		// 2. 로그인 사용자 정보 파싱 (JWT)
		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		Integer userNo = (user != null) ? user.getUserNo() : null;

		// 3. 판매자 여부 계산
		boolean isOwnItem = (userNo != null && userNo.equals(item.getSellerNo()));

		// 4. JSP에 전달
		model.addAttribute("item", item);
		model.addAttribute("userNo", userNo);
		model.addAttribute("isOwnItem", isOwnItem);

		return "item/itemDetail";
	}
}
