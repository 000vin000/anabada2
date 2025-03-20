package kr.co.anabada.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {
	@GetMapping
	public String signup() {
		return "userphase/signup";
	}
	
	@GetMapping("/individual")
	public String signupIdividual() {
		return "userphase/signup_individual";
	}
	
	@GetMapping("/brand")
	public String signupBrand() {
		return "userphase/signup_brand";
	}
}
