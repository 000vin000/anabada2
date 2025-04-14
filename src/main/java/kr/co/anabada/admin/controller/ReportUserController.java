package kr.co.anabada.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportUserController {
	@GetMapping("/report")
	public String reportUser() {
		return "admin/report";
	}
	
	@GetMapping("/report/success")
	public String reportSuccess() {
		return "admin/reportSuccess";
	}
}
