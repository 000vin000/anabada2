package kr.co.anabada.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.anabada.mypage.dto.PasswordChangeForm;
import kr.co.anabada.mypage.service.UpdateInfoService;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.UserService;

@Controller
@RequestMapping("/mypage")
public class UpdateInfoController {

    @Autowired
    private UserService userService;

    // 회원 탈퇴 관련 서비스 + 업데이트에 이용 
    @Autowired
    private UpdateInfoService updateInfoService; 
    
    // 회원정보 수정 페이지 표시
    @GetMapping("/updateinfo")
    public String showUpdateInfoForm(HttpSession session, Model model) {
    	
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Boolean passwordVerified = (Boolean) session.getAttribute("passwordVerified");
        

        // 로그인 상태 및 비밀번호 확인 여부 검사
        if (loggedInUser == null || passwordVerified == null || !passwordVerified) {
            return "redirect:/mypage/passwordcheck?redirectTo=/mypage/updateinfo";
        }

        // 전화번호 분리
        String userPhone = loggedInUser.getUserPhone();
        String phone1 = "";
        String phone2 = "";
        String phone3 = "";

        if (userPhone != null && userPhone.length() >= 10) {
            phone1 = userPhone.substring(0, 3);
            if (userPhone.length() == 10) {
                phone2 = userPhone.substring(3, 6);
                phone3 = userPhone.substring(6);
            } else if (userPhone.length() == 11) {
                phone2 = userPhone.substring(3, 7);
                phone3 = userPhone.substring(7);
            }
        }

        model.addAttribute("user", loggedInUser);
        model.addAttribute("phone1", phone1);
        model.addAttribute("phone2", phone2);
        model.addAttribute("phone3", phone3);
        //메일 정보 추가
        model.addAttribute("userEmail", loggedInUser.getUserEmail());
        model.addAttribute("userAdd", loggedInUser.getUserAdd());

        System.out.println("UpdateInfoController - showUpdateInfoForm: User phone number: " + loggedInUser.getUserPhone());
        System.out.println("UpdateInfoController - showUpdateInfoForm: Separated phone numbers: " + phone1 + ", " + phone2 + ", " + phone3);

        return "mypage/updateinfo"; 
    }

    @PostMapping("/updateinfo")
    public String updateUserInfo(@ModelAttribute("user") User updatedUser,  // @Valid 제거
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model,
                                 @RequestParam("userPhone1") String phone1,
                                 @RequestParam("userPhone2") String phone2,
                                 @RequestParam("userPhone3") String phone3,
                                 @RequestParam("detailAddress") String detailAddress) {
        System.out.println("UpdateInfoController - updateUserInfo: 메서드 시작");
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            System.out.println("UpdateInfoController - updateUserInfo: 로그인된 사용자 없음");
            return "redirect:/user/login";
        }

        System.out.println("UpdateInfoController - updateUserInfo: 현재 로그인 사용자 정보: " + loggedInUser);

        // 기존 비밀번호 유지
        updatedUser.setUserPw(loggedInUser.getUserPw());
        updatedUser.setUserPw2(loggedInUser.getUserPw());  // userPw2도 설정

        // 전화번호 조합 및 설정
        String phoneNumber = phone1 + phone2 + phone3;
        System.out.println("UpdateInfoController - updateUserInfo: 조합된 전화번호: " + phoneNumber);
        
        if (!phoneNumber.matches("^\\d{10,11}$")) {
            System.out.println("UpdateInfoController - updateUserInfo: 전화번호 형식 오류");
            bindingResult.rejectValue("userPhone", "error.userPhone", "올바른 전화번호 형식이 아닙니다.");
        } else {
            updatedUser.setUserPhone(phoneNumber);
        }
        
        if (!detailAddress.isEmpty()) {
            updatedUser.setUserAdd(updatedUser.getUserAdd() + " " + detailAddress);
        }
        System.out.println("UpdateInfoController - updateUserInfo: 업데이트된 주소: " + updatedUser.getUserAdd());

        // 수동으로 유효성 검사 수행
        if (updatedUser.getUserName() == null || updatedUser.getUserName().trim().isEmpty()) {
            bindingResult.rejectValue("userName", "error.userName", "이름은 필수 입력값입니다.");
        }
        if (updatedUser.getUserNick() == null || updatedUser.getUserNick().trim().isEmpty()) {
            bindingResult.rejectValue("userNick", "error.userNick", "닉네임은 필수 입력값입니다.");
        }
        // 필요한 다른 필드들에 대해서도 유사한 검사 수행

        // 닉네임, 전화번호 중복 검사 (변경된 경우에만)
        if (!loggedInUser.getUserNick().equals(updatedUser.getUserNick())) {
            System.out.println("UpdateInfoController - updateUserInfo: 닉네임 변경 감지");
            if (userService.isUserNickDuplicate(updatedUser.getUserNick())) {
                System.out.println("UpdateInfoController - updateUserInfo: 닉네임 중복");
                bindingResult.rejectValue("userNick", "error.userNick", "이미 사용 중인 닉네임입니다.");
            }
        }

        if (!loggedInUser.getUserPhone().equals(updatedUser.getUserPhone())) {
            System.out.println("UpdateInfoController - updateUserInfo: 전화번호 변경 감지");
            if (userService.isUserPhoneDuplicate(updatedUser.getUserPhone())) {
                System.out.println("UpdateInfoController - updateUserInfo: 전화번호 중복");
                bindingResult.rejectValue("userPhone", "error.userPhone", "이미 사용 중인 전화번호입니다.");
            }
        }
        
        // 유효성 검사 오류 확인 및 처리
        if (bindingResult.hasErrors()) {
            System.out.println("UpdateInfoController - updateUserInfo: 유효성 검사 오류");
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "mypage/updateinfo";
        }

        // 기존 이메일 정보 유지
        updatedUser.setUserEmail(loggedInUser.getUserEmail());

        System.out.println("UpdateInfoController - updateUserInfo: 업데이트할 사용자 정보: " + updatedUser);

        String result = updateInfoService.updateUserInfo(updatedUser, loggedInUser.getUserId());
        System.out.println("UpdateInfoController - updateUserInfo: 업데이트 결과: " + result);
        
        if ("회원정보 변경 성공".equals(result)) {
            session.setAttribute("loggedInUser", updatedUser);
            model.addAttribute("successMessage", "회원정보가 성공적으로 변경되었습니다.");
        } else {
            model.addAttribute("error", result);
        }
        return "mypage/updateinfo";
    }


    
    @GetMapping("/changePassword")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        return "mypage/changePassword";
    }

    //비번번경
    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute("passwordChangeForm") @Valid PasswordChangeForm form,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "mypage/changePassword";
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }

        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            bindingResult.rejectValue("confirmNewPassword", "error.confirmNewPassword", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return "mypage/changePassword";
        }

        String result = updateInfoService.changePassword(loggedInUser.getUserId(), form.getCurrentPassword(), form.getNewPassword());
        
        if ("비밀번호가 성공적으로 변경되었습니다.".equals(result)) {
            model.addAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
            return "mypage/changePassword"; // 비밀번호 변경 페이지로 이동
        } else {
            model.addAttribute("error", result);
            return "mypage/changePassword";
        }
    }

    
    // 회원 탈퇴 관련 서비스    
    @GetMapping("/deactivate")
    public String showDeactivateForm(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Boolean passwordVerified = (Boolean) session.getAttribute("passwordVerified");

        // 로그인 상태 및 비밀번호 확인 여부 검사
        if (loggedInUser == null || passwordVerified == null || !passwordVerified) {
            return "redirect:/mypage/passwordcheck?redirectTo=/mypage/deactivate";
        }

        return "mypage/deactivate"; // deactivate.jsp 페이지로 이동
    }

    @PostMapping("/deactivate")
    public String deactivateUser(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }

        boolean success = updateInfoService.deactivateUser(loggedInUser.getUserNo());
        if (success) {
            session.invalidate(); // 세션 무효화 처리
            return "redirect:/";  // 메인 페이지로 리다이렉트
        } else {
            model.addAttribute("error", "회원 탈퇴 처리 중 오류가 발생했습니다.");
            return "mypage/updateinfo";
        }
    }
}