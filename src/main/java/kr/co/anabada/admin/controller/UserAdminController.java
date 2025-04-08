package kr.co.anabada.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class UserAdminController {

    @GetMapping("/adminRole")
    public String userAdminPage() {
        return "admin/adminRole"; // 뷰 페이지 반환
    }
}
