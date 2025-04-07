package kr.co.anabada.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.item.entity.Item.ItemStatus;
import kr.co.anabada.user.dto.UserProfileDTO;
import kr.co.anabada.user.service.UserProfileService;

@Controller
@RequestMapping("user/profile/{targetUserNo}")
public class UserProfileController {
	@Autowired
	UserProfileService userProfileService;
	
	@GetMapping
	public String getUserProfile(@PathVariable Integer targetUserNo, HttpServletRequest req, Model model) {
		UserProfileDTO profile = userProfileService.getUserProfileDTO(targetUserNo);

		model.addAttribute("profile", profile);
		model.addAttribute("itemStatuses", ItemStatus.values());

		return "user/userProfile";
	}
}
