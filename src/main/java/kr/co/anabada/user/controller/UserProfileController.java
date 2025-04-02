package kr.co.anabada.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.dto.UserProfileDTO;
import kr.co.anabada.user.service.UserProfileService;
import kr.co.anabada.user.service.UserService;

@Controller
@RequestMapping("user/profile/{targetUserNo}")
public class UserProfileController {
	@Autowired
	UserProfileService userProfileService;
//	@Autowired
//	private JwtAuthHelper jwtAuthHelper;
	@Autowired
	private UserService userService;

	@GetMapping
	public String getUserProfile(@PathVariable Integer targetUserNo, Model model, HttpServletRequest req) {
//		UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
		Integer userNo = userService.findByUserId("hj-rxl").getUserNo();
		UserProfileDTO profile = userProfileService.getUserProfileDTO(targetUserNo);

//		model.addAttribute("userNo", (user != null ? user.getUserNo() : 0));
		model.addAttribute("userNo", userNo);
		model.addAttribute("profile", profile);

		System.out.println(userNo);
		System.out.println(profile);

		return "user/userProfile";
	}

	@GetMapping("/sells")
	@ResponseBody
	public Page<UserProfileDTO.ItemSummaryDTO> getSellItems(
			@PathVariable Integer targetUserNo,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "recent") String sort) {

		return userProfileService.getSellSummaryDTOs(targetUserNo, page, size, sort);
	}

	@GetMapping("/buys")
	@ResponseBody
	public Page<UserProfileDTO.ItemSummaryDTO> getBuyItems(
			@PathVariable Integer targetUserNo,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "recent") String sort) {

		return userProfileService.getBuySummaryDTOs(targetUserNo, page, size, sort);
	}
}
