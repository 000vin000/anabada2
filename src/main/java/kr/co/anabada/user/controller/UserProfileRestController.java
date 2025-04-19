package kr.co.anabada.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.user.dto.PendingItemSummaryDTO;
import kr.co.anabada.user.dto.UserProfileDTO;
import kr.co.anabada.user.dto.UserProfileDashboardDTO;
import kr.co.anabada.user.service.UserProfileService;

@RestController
@RequestMapping("/api/user/profile")
public class UserProfileRestController {
	@Autowired
	UserProfileService service;
	
	@GetMapping
	public Integer getCurrentUser(HttpServletRequest req) {
		return service.getCurrentUser(req);
	}

	@GetMapping("/{targetUserNo}/sells")
	public ResponseEntity<Page<UserProfileDTO.ItemSummaryDTO>> getSellItems(
			@PathVariable Integer targetUserNo, HttpServletRequest req,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "all") String status,
			@RequestParam(defaultValue = "recent") String sort) {

		Integer loggedInUserNo = service.getCurrentUser(req);
		boolean isOwnProfile = loggedInUserNo > 0 && loggedInUserNo.equals(targetUserNo);
		return ResponseEntity.ok(service.getSellItems(targetUserNo, isOwnProfile, page, size, status, sort));
	}

	@GetMapping("/{targetUserNo}/buys")
	public ResponseEntity<Page<UserProfileDTO.ItemSummaryDTO>> getBuyItems(
			@PathVariable Integer targetUserNo, HttpServletRequest req,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "all") String status,
			@RequestParam(defaultValue = "recent") String sort) {

		Integer loggedInUserNo = service.getCurrentUser(req);
		boolean isOwnProfile = loggedInUserNo > 0 && loggedInUserNo.equals(targetUserNo);
		return ResponseEntity.ok(service.getBuyItems(targetUserNo, isOwnProfile, page, size, status, sort));
	}
	
	@GetMapping("/{targetUserNo}/pending-sells")
	public ResponseEntity<List<PendingItemSummaryDTO>> getPendingSellItems(
			@PathVariable Integer targetUserNo, HttpServletRequest req) {
		if (service.getCurrentUser(req) != targetUserNo) {
			throw new AccessDeniedException("조회 권한이 없습니다.");
		}
		List<PendingItemSummaryDTO> pendingItems = service.getPendingSellConfirmItems(targetUserNo, req);
		return ResponseEntity.ok(pendingItems);
	}

	@GetMapping("/{targetUserNo}/pending-buys")
	public ResponseEntity<List<PendingItemSummaryDTO>> getPendingBuyItems(
			@PathVariable Integer targetUserNo, HttpServletRequest req) {
		if (service.getCurrentUser(req) != targetUserNo) {
			throw new AccessDeniedException("조회 권한이 없습니다.");
		}
		List<PendingItemSummaryDTO> pendingItems = service.getPendingBuyConfirmItems(targetUserNo, req);
		return ResponseEntity.ok(pendingItems);
	}
	
	@GetMapping("/{targetUserNo}/dashboard")
	public ResponseEntity<UserProfileDashboardDTO> getDashboard(
			@PathVariable Integer targetUserNo) {
		// TODO 본인 프로필에서만 접근 가능하도록 제한
		UserProfileDashboardDTO dashboard = service.getUserProfileDashboardDTO(targetUserNo);
		return ResponseEntity.ok(dashboard);
	}
}
