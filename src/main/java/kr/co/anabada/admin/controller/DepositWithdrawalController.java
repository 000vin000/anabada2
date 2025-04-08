package kr.co.anabada.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DepositWithdrawalController {
	@GetMapping("/admin/depositWithdrawal")
	public String depositWithdrawal() {
		return "admin/depositWithdrawal";
	}
}
