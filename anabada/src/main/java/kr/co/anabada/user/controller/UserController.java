package kr.co.anabada.user.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.service.UserService;
import kr.co.anabada.user.entity.EmailVerificationInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/user")
public class UserController {
	
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserService userService;
    
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @GetMapping("/join")
    public String showJoinForm(Model model, HttpSession session) {
        EmailVerificationInfo verificationInfo = (EmailVerificationInfo) session.getAttribute("emailVerificationInfo");

        System.out.println("showJoinForm - verificationInfo: " + verificationInfo);

        if (verificationInfo == null || isVerificationExpired(verificationInfo)) {
            System.out.println("이메일 인증이 필요합니다. 인증 페이지로 리다이렉트합니다.");
            return "redirect:/email/verification";
        }

        model.addAttribute("email", verificationInfo.getEmail());
        model.addAttribute("user", new User());

        System.out.println("회원가입 폼을 표시합니다.");
        return "user/join";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }
    
    @PostMapping("/join")
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult,
                               @RequestParam("userPhone1") String phone1,
                               @RequestParam("userPhone2") String phone2,
                               @RequestParam("userPhone3") String phone3,
                               Model model,
                               HttpSession session) {
        
        String fullPhone = phone1 + phone2 + phone3;
        user.setUserPhone(fullPhone);      
        logger.info("회원가입 시도: {}", user.toString());
        
        if (!fullPhone.matches("^\\d{10,11}$")) {
            bindingResult.rejectValue("userPhone", "error.userPhone", "올바른 전화번호 형식이 아닙니다.");
        }
        
        EmailVerificationInfo verificationInfo = (EmailVerificationInfo) session.getAttribute("emailVerificationInfo");
        if (verificationInfo == null || isVerificationExpired(verificationInfo)) {
            System.out.println("이메일 인증이 만료되었습니다. 인증 페이지로 리다이렉트합니다.");
            return "redirect:/email/verification";
        }

        if (!user.getUserPw().equals(user.getUserPw2())) {
            bindingResult.rejectValue("userPw2", "error.userPw2", "비밀번호가 일치하지 않습니다.");
        }
        if (!pattern.matcher(user.getUserPw()).matches()) {
            bindingResult.rejectValue("userPw", "error.userPw", "비밀번호는 특수문자, 문자, 숫자를 포함하며 6자 이상이어야 합니다.");
        }        
        if (userService.isUserEmailDuplicate(user.getUserEmail())) {
            bindingResult.rejectValue("userEmail", "error.userEmail", "이미 사용 중인 이메일입니다.");
        }
        if (userService.isUserPhoneDuplicate(user.getUserPhone())) {
            bindingResult.rejectValue("userPhone", "error.userPhone", "이미 사용 중인 전화번호입니다.");
        }
        
        if (bindingResult.hasErrors()) {
            return "user/join";
        }
        try {
            // UserService.joinUser 호출 후 로그 추가
            String joinResult = userService.joinUser(user);
            logger.info("회원가입 결과: {}", joinResult); // 추가된 로그
            
            if ("회원가입 성공".equals(joinResult)) {
                session.removeAttribute("emailVerificationInfo");
                model.addAttribute("successMessage", "회원가입이 성공적으로 완료되었습니다.");
                return "user/login"; // 로그인 페이지로 이동
            } else {
                model.addAttribute("error", joinResult);
                return "user/join";
            }
        } catch (Exception e) {
            // 예외 처리 시 로거를 사용하여 상세한 오류 정보 기록
            logger.error("회원가입 중 예외 발생", e);
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "user/join";
        }
    }
   
    @GetMapping("/check-duplicate/{field}")
    @ResponseBody
    public Map<String, Boolean> checkDuplicate(@PathVariable String field, @RequestParam String value) {
        boolean isDuplicate = false;

        switch (field) {
            case "userId":
                isDuplicate = userService.isUserIdDuplicate(value);
                break;
            case "userNick":
                isDuplicate = userService.isUserNickDuplicate(value);
                break;
            case "userEmail":
                isDuplicate = userService.isUserEmailDuplicate(value);
                break;
            case "userPhone":
                value = value.replaceAll("-", "");
                if (!value.matches("^\\d{10,11}$")) {
                    Map<String, Boolean> invalidResponse = new HashMap<>();
                    invalidResponse.put("isDuplicate", false);
                    invalidResponse.put("invalidFormat", true); 
                    return invalidResponse;
                }
                isDuplicate = userService.isUserPhoneDuplicate(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field for duplication check: " + field);
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return response;
    }

    @PostMapping("/login")
    public String loginUser(User user, HttpSession session, Model model) {
        try {
            String result = userService.loginUser(user.getUserId(), user.getUserPw());
            if ("로그인 성공".equals(result)) {
                User fullUserInfo = userService.getUserByUserId(user.getUserId());
                session.setAttribute("loggedInUser", fullUserInfo);
                return "redirect:/";
            } else {
                model.addAttribute("error", result);
                return "user/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "로그인 중 문제가 발생했습니다. 관리자에게 문의하세요.");
            return "user/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    private boolean isVerificationExpired(EmailVerificationInfo info) {
        return LocalDateTime.now().isAfter(info.getVerificationTime().plusMinutes(3));
    }
}