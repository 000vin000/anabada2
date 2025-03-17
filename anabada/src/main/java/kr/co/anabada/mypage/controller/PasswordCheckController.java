package kr.co.anabada.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.co.anabada.mypage.service.PasswordCheckService;
import kr.co.anabada.user.entity.User;

@Controller
@RequestMapping("/mypage")
public class PasswordCheckController {

    @Autowired
    private PasswordCheckService passwordCheckService;

    @GetMapping("/passwordcheck")
    public String showPasswordCheckForm(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        return "mypage/passwordCheck";
    }

    @PostMapping("/passwordcheck")
    public String checkPassword(String userPw, String redirectTo, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }

        boolean isPasswordCorrect = passwordCheckService.checkPassword(userPw, loggedInUser.getUserId());
        if (isPasswordCorrect) {
            session.setAttribute("passwordVerified", true);
            return "redirect:" + redirectTo;
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "mypage/passwordCheck";
        }
    }
}
