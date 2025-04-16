package kr.co.anabada.admin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.repository.AdminRepository;
import kr.co.anabada.admin.service.AdminService;
import kr.co.anabada.jwt.JwtAuthHelper;
import kr.co.anabada.jwt.UserTokenInfo;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.entity.User.UserType;
import kr.co.anabada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAdminRestController {
	@Autowired
    private JwtAuthHelper jwtAuthHelper;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@GetMapping("/check-auth")
    public ResponseEntity<?> checkAdminAuth(HttpServletRequest request) {
        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "토큰 없음"));
        }

        Admin admin = adminService.findByUserNo(user.getUserNo());
        if (admin == null || Boolean.TRUE.equals(admin.getCanManageFinances()) == false || admin.getAdminLevel() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "접근 권한 없음"));
        }

        return ResponseEntity.ok(Map.of(
            "message", "접근 가능",
            "adminDept", admin.getAdminDept()
        ));
    }

	 @GetMapping("/search")
	    public ResponseEntity<?> searchUser(@RequestParam("userId") String userId, HttpServletRequest req) {
		  // 관리자 로그인 확인
	        UserTokenInfo user = jwtAuthHelper.getUserFromRequest(req);
	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "관리자 로그인 필요"));
	        }

	        Admin admin = adminService.findByUserNo(user.getUserNo());
	        if (admin == null || Boolean.TRUE.equals(admin.getCanManageFinances()) == false) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT)
	                    .body(Map.of("error", "관리자 권한 필요"));
	        }   
		 
		 // 사용자 조회 로직
	        User targetUser = userRepository.findByUserId(userId);
	        if (targetUser != null) {
	            String userType = (targetUser.getUserType() != null) ? targetUser.getUserType().name() : "UNKNOWN";
	            Map<String, Object> result = Map.of(
	                "userId", targetUser.getUserId(),
	                "userType", userType
	            );
	            return ResponseEntity.ok(result);
	        } else {
	            return ResponseEntity.status(404).body(Map.of("error", "사용자를 찾을 수 없습니다."));
	        }
	    }

	 @PostMapping("/change-role")
	    public ResponseEntity<?> changeUserRole(@RequestBody Map<String, String> data, HttpServletRequest req) {
	        // 관리자 로그인 확인
	        UserTokenInfo authUser = jwtAuthHelper.getUserFromRequest(req);
	        if (authUser == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 로그인 필요");
	        }

	        Admin authAdmin = adminService.findByUserNo(authUser.getUserNo());
	        if (authAdmin == null) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한 없음");
	        }

	        String userId = data.get("userId");
	        String newRole = data.get("newRole");
	        String adminDept = data.getOrDefault("adminDept", "관리부서");

	        User user = userRepository.findByUserId(userId);
	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
	        }

	        try {
	            UserType role = UserType.valueOf(newRole);
	            user.setUserType(role);
	            userRepository.save(user);

	            // ADMIN 권한 부여
	            if (role == UserType.ADMIN) {
	                Admin admin = adminRepository.findByUser(user);
	                if (admin == null) {
	                    admin = Admin.createDefaultAdmin(user, adminDept);  // 정적 팩토리 메서드 사용
	                } else {
	                    admin.setAdminDept(adminDept);  // 관리자 부서 업데이트
	                }
	                adminRepository.save(admin);
	            } else {
	                // 관리자 권한 삭제
	                Admin existingAdmin = adminRepository.findByUser(user);
	                if (existingAdmin != null) {
	                    adminRepository.delete(existingAdmin);
	                }
	            }

	            boolean isSelfChange = authUser.getUserId().equals(userId);
	            return ResponseEntity.ok(Map.of("message", "권한 변경 성공", "selfChange", isSelfChange));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
	        }
	    }
	}
