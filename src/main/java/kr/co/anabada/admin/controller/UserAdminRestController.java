package kr.co.anabada.admin.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.repository.AdminRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.entity.User.UserType;
import kr.co.anabada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAdminRestController {
	private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam("userId") String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            // userType이 null인 경우 대비
            String userType = (user.getUserType() != null) ? user.getUserType().name() : "UNKNOWN";

            Map<String, Object> result = Map.of(
                "userId", user.getUserId(),
                "userType", userType
            );
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "사용자를 찾을 수 없습니다."));
        }
    }

    @PostMapping("/change-role")
    public ResponseEntity<?> changeUserRole(@RequestBody Map<String, String> data) {
        String userId = data.get("userId");
        String newRole = data.get("newRole");
        String adminDept = data.getOrDefault("adminDept", "관리부서");

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("사용자 없음");
        }

        try {
            UserType role = UserType.valueOf(newRole);
            user.setUserType(role);
            userRepository.save(user);

            if (role == UserType.ADMIN) {
                Admin admin = adminRepository.findByUser(user);
                if (admin == null) {
                    admin = new Admin();
                    admin.setUser(user);
                    admin.setAdminLevel((byte) 1);
                    admin.setCanManageIndivisual(true);
                    admin.setCanManageBrand(true);
                    admin.setCanManageFinances(true);
                    admin.setAdminId(user.getUserId());
                    admin.setAdminPw(user.getUserPw()); 
                }
                admin.setAdminDept(adminDept);
                adminRepository.save(admin);
            } else {
                Admin existingAdmin = adminRepository.findByUser(user);
                if (existingAdmin != null) {
                    adminRepository.delete(existingAdmin);
                    System.out.println("admin 테이블에서 권한 삭제 완료");
                }
            }

            return ResponseEntity.ok(Map.of("message", "권한 변경 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
        }
    }

}
