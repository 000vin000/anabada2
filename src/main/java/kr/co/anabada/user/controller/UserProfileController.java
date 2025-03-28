package kr.co.anabada.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.anabada.user.dto.UserProfileDTO;
import kr.co.anabada.user.service.UserProfileService;

@Controller
@RequestMapping("user/profile/{userNo}")
public class UserProfileController {
	@Autowired
	UserProfileService userProfileService;

	@GetMapping
	public String getUserProfile(@PathVariable Integer userNo, Model model) {
		UserProfileDTO profile = userProfileService.getUserProfileDTO(userNo);
		model.addAttribute("profile", profile);
		return "user/userProfile";
	}
}
