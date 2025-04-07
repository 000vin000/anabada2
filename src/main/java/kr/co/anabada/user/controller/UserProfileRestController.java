package kr.co.anabada.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.user.dto.UserProfileDTO;
import kr.co.anabada.user.service.UserProfileService;

@RestController
@RequestMapping("/api/user/profile")
public class UserProfileRestController {
	@Autowired
	UserProfileService userProfileService;
	
	@GetMapping
	public Integer getCurrentUser(HttpServletRequest req) {
		return userProfileService.getCurrentUser(req);
	}

	@GetMapping("/{targetUserNo}/sells")
	public Page<UserProfileDTO.ItemSummaryDTO> getSellItems(
			@PathVariable Integer targetUserNo, HttpServletRequest req,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "all") String status,
			@RequestParam(defaultValue = "recent") String sort) {

		Integer loggedInUserNo = userProfileService.getCurrentUser(req);
		boolean isOwnProfile = loggedInUserNo > 0 && loggedInUserNo.equals(targetUserNo);
		return userProfileService.getSellItems(targetUserNo, isOwnProfile, page, size, status, sort);
	}

	@GetMapping("/{targetUserNo}/buys")
	public Page<UserProfileDTO.ItemSummaryDTO> getBuyItems(
			@PathVariable Integer targetUserNo, HttpServletRequest req,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "all") String status,
			@RequestParam(defaultValue = "recent") String sort) {

		Integer loggedInUserNo = userProfileService.getCurrentUser(req);
		boolean isOwnProfile = loggedInUserNo > 0 && loggedInUserNo.equals(targetUserNo);
		return userProfileService.getBuyItems(targetUserNo, isOwnProfile, page, size, status, sort);
	}
}
